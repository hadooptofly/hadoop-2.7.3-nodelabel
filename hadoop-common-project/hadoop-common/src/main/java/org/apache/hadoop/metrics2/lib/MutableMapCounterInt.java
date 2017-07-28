/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.metrics2.lib;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics2.MetricsInfo;
import org.apache.hadoop.metrics2.MetricsRecordBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A mutable int counter for implementing metrics sources
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableMapCounterInt extends MutableMapCounter {
  private Map<String, MutableCounterInt> value = new HashMap<String, MutableCounterInt>();

  MutableMapCounterInt(MetricsInfo info, Map<String, MutableCounterInt> initValue) {
    super(info);
    this.value = initValue;
  }

  @Override
  public void incr(String label) {
    incr(label, 1);
  }

  /**
   * Increment the value by a delta
   * @param delta of the increment
   */
  public synchronized void incr(String label, int delta) {
    value.get(label).getValue().addAndGet(delta);
    setChanged();
  }

  public int value(String label) {
    return value.get(label).value();
  }

  @Override
  public void snapshot(MetricsRecordBuilder builder, boolean all) {
    if (all || changed()) {
      builder.addCounter(info(), value());
      clearChanged();
    }
  }

}
