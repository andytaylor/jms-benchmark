/**
 * Copyright (C) 2009-2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.jmsbenchmark

import javax.jms.{Session, Destination, ConnectionFactory}
import org.apache.qpid.client._
import org.apache.qpid.jms.ConnectionURL

object QpidScenario {
  def main(args:Array[String]):Unit = {
    val scenario = new org.fusesource.jmsbenchmark.QpidScenario
    scenario.url = "amqp://admin:password@clientid/192.168.124.147?brokerlist='tcp://192.168.124.147:5672'"
    scenario.user_name = "admin"
    scenario.password = "password"
    scenario.display_errors = true
    scenario.message_size = 10
    scenario.destination_type = "topic"
    scenario.producers = 1
    scenario.consumers = 10
    scenario.producer_sleep_=(1000)
    scenario.run()
  }
}

/**
 * <p>
 * Qpid implementation of the JMS Scenario class.
 * </p>
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
class QpidScenario extends JMSClientScenario {

  override protected def factory:ConnectionFactory = {
    val client_id = user_name
    val virtual_host = ""
    val x = new AMQConnectionURL(url)
    x.setClientName(client_id)
    x.setVirtualHost(virtual_host)
    new AMQConnectionFactory(x)
  }

  override protected def destination(i:Int):Destination = destination_type match {
    case "queue" =>
      new AMQQueue(indexed_destination_name(i)+";{create:always,node:{durable:true}}")
    case "topic" =>
      new AMQTopic("amq.topic/"+indexed_destination_name(i))
    case _ =>
      sys.error("Unsuported destination type: "+destination_type)
  }

  override def load_start_rendezvous(client: JMSClient, session: Session) {
    // add code here that executes before scenario load starts getting applied.
    super.load_start_rendezvous(client, session)
    // add code here that executes after scenario load starts getting applied.
  }

}
