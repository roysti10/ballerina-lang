/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballerinalang.test.javainterop;

import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.BRunUtil;
import org.ballerinalang.test.CompileResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.ballerinalang.test.BAssertUtil.validateError;

/**
 * Test cases for dependently typed function signatures derived from contextual type.
 *
 * @since 2.0.0
 */
public class InferredDependentlyTypeFuncSignatureTest {

    private CompileResult result;
    private static final String INVALID_RETURN_TYPE_ERROR = "invalid return type: members of a dependently-typed " +
            "union type with an inferred typedesc parameter should have disjoint basic types";

    @BeforeClass
    public void setup() {
        result = BCompileUtil.compile("test-src/javainterop/inferred_dependently_typed_func_signature.bal");
    }

    @Test(dataProvider = "FunctionNames")
    public void testVariableTypeAsReturnType(String funcName) {
        BRunUtil.invoke(result, funcName);
    }

    @DataProvider(name = "FunctionNames")
    public Object[][] getFuncNames() {
        return new Object[][]{
                {"testRecordVarRef"},
                {"testVarRefInMapConstraint"},
                {"testTupleTypes"},
                {"testSimpleTypes"},
                {"testArrayTypes"},
                {"testStream"},
                {"testTable"},
                {"testFunctionPointers"},
                {"testTypedesc"},
                {"testFuture"},
                {"testComplexTypes"},
                {"testRuntimeCastError"},
                {"testFunctionAssignment"}
//                {"testCastingForInvalidValues"}
        };
    }

    @Test
    public void testDependentlyTypedFunctionWithInferredTypedescValueNegative() {
        CompileResult negativeResult =
                BCompileUtil.compile("test-src/javainterop/inferred_dependently_typed_func_signature_negative.bal");
        int index = 0;
        validateError(negativeResult, index++, "incompatible type for parameter 'rowType' with inferred " +
                "typedesc value: expected 'record {| int...; |}', found 'OpenRecord'", 20, 37);
        validateError(negativeResult, index++, "incompatible types: expected 'typedesc<record {| int...; |}>', found " +
                "'typedesc<OpenRecord>'", 21, 48);
        validateError(negativeResult, index++, "cannot have more than one defaultable parameter with an inferred " +
                "typedesc default", 28, 1);
        validateError(negativeResult, index++, "cannot have more than one defaultable parameter with an inferred " +
                "typedesc default", 32, 5);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 42, 51);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 44, 46);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 46, 52);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 48, 65);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 50, 64);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 52, 53);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 54, 66);
        validateError(negativeResult, index++, INVALID_RETURN_TYPE_ERROR, 56, 49);
        Assert.assertEquals(index, negativeResult.getErrorCount());
    }
}
