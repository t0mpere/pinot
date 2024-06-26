/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pinot.core.startree;

import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import java.util.List;
import org.apache.pinot.core.operator.filter.predicate.PredicateEvaluator;


/**
 * Represents a composite predicate.
 *
 * A composite predicate evaluator represents a single predicate evaluator or multiple predicate evaluators conjoined
 * with OR. Each predicate evaluator is associated with a boolean value indicating whether the predicate is negated.
 * Consider the given predicate: (d1 > 10 OR NOT d1 > 50). A composite predicate will represent two predicates:
 * (d1 > 10) and NOT(d1 > 50) and represent that they are related by the operator OR.
 */
public class CompositePredicateEvaluator {
  private final List<ObjectBooleanPair<PredicateEvaluator>> _predicateEvaluators;

  public CompositePredicateEvaluator(List<ObjectBooleanPair<PredicateEvaluator>> predicateEvaluators) {
    assert !predicateEvaluators.isEmpty();
    _predicateEvaluators = predicateEvaluators;
  }

  public List<ObjectBooleanPair<PredicateEvaluator>> getPredicateEvaluators() {
    return _predicateEvaluators;
  }

  /**
   * Applies a dictionary id to the composite predicate evaluator. Returns {@code true} if the dictionary id matches any
   * predicate evaluator, {@code false} otherwise.
   */
  public boolean apply(int dictId) {
    for (ObjectBooleanPair<PredicateEvaluator> predicateEvaluator : _predicateEvaluators) {
      if (predicateEvaluator.left().applySV(dictId) != predicateEvaluator.rightBoolean()) {
        return true;
      }
    }
    return false;
  }
}
