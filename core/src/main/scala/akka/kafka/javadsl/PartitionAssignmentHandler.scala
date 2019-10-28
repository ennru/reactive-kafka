/*
 * Copyright (C) 2014 - 2016 Softwaremill <http://softwaremill.com>
 * Copyright (C) 2016 - 2019 Lightbend Inc. <http://www.lightbend.com>
 */

package akka.kafka.javadsl

import akka.kafka.RestrictedConsumer
import akka.annotation.ApiMayChange
import org.apache.kafka.common.TopicPartition

/**
 * The API is new and may change in further releases.
 *
 * Allows the user to execute user code when Kafka rebalances partitions between consumers, or an Alpakka Kafka consumer is stopped.
 * Use with care: These callbacks are called synchronously on the same thread Kafka's `poll()` is called.
 * A warning will be logged if a callback takes longer than the configured `partition-handler-warning`.
 *
 * There is no point in calling `Committable`'s commit methods as their committing won't be executed as long as any of
 * the callbacks in this class are called. Calling `commitSync` on the passed [[akka.kafka.RestrictedConsumer]] is available.
 *
 * This complements the methods of Kafka's [[org.apache.kafka.clients.consumer.ConsumerRebalanceListener ConsumerRebalanceListener]] with
 * an `onStop` callback which is called before `Consumer.close`.
 */
@ApiMayChange
trait PartitionAssignmentHandler {

  /**
   * See [[org.apache.kafka.clients.consumer.ConsumerRebalanceListener#onPartitionsRevoked]]
   *
   * @param revokedTps The list of partitions that were revoked from the consumer
   * @param consumer The [[akka.kafka.RestrictedConsumer]] gives some access to the internally used [[org.apache.kafka.clients.consumer.Consumer Consumer]]
   */
  def onRevoke(revokedTps: java.util.Set[TopicPartition], consumer: RestrictedConsumer): Unit

  /**
   * See [[org.apache.kafka.clients.consumer.ConsumerRebalanceListener#onPartitionsAssigned]]
   *
   * @param assignedTps The list of partitions that are now assigned to the consumer (may include partitions previously assigned to the consumer)
   * @param consumer The [[akka.kafka.RestrictedConsumer]] gives some access to the internally used [[org.apache.kafka.clients.consumer.Consumer Consumer]]
   */
  def onAssign(assignedTps: java.util.Set[TopicPartition], consumer: RestrictedConsumer): Unit

  /**
   * Called before a consumer is closed.
   * See [[org.apache.kafka.clients.consumer.ConsumerRebalanceListener#onPartitionsRevoked]]
   *
   * @param currentTps The list of partitions that are currently assigned to the consumer
   * @param consumer The [[akka.kafka.RestrictedConsumer]] gives some access to the internally used [[org.apache.kafka.clients.consumer.Consumer Consumer]]
   */
  def onStop(currentTps: java.util.Set[TopicPartition], consumer: RestrictedConsumer): Unit

}