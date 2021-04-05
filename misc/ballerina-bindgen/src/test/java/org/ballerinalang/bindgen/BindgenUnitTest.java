/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.bindgen;

import io.ballerina.compiler.syntax.tree.SyntaxTree;
import org.ballerinalang.bindgen.exceptions.BindgenException;
import org.ballerinalang.bindgen.model.JClass;
import org.ballerinalang.bindgen.model.JError;
import org.ballerinalang.bindgen.utils.BindgenEnv;
import org.ballerinalang.bindgen.utils.BindgenFileGenerator;
import org.ballerinalang.formatter.core.Formatter;
import org.ballerinalang.formatter.core.FormatterException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test the Ballerina syntax tree generated by the tool.
 *
 * @since 2.0.0
 */
public class BindgenUnitTest {

    private final Path resourceDirectory = Paths.get("src").resolve("test").resolve("resources").toAbsolutePath();

    @Test(description = "Test the constructor bindings generated")
    public void constructorMapping() throws FormatterException, ClassNotFoundException, BindgenException, IOException {
        Path constructorFilePath = Paths.get(resourceDirectory.toString(), "unit-test-resources", "constructors.bal");
        String constructors = Files.readString(resourceDirectory.resolve(constructorFilePath));
        SyntaxTree cSyntaxTree = getBindingsGenerator().generate(new JClass(this.getClass().getClassLoader()
                .loadClass("org.ballerinalang.bindgen.ConstructorsTestResource"), getBindgenEnv()));
        Assert.assertEquals(Formatter.format(cSyntaxTree.toSourceCode()), Formatter.format(constructors));
        Assert.assertFalse(cSyntaxTree.hasDiagnostics());
    }

    @Test(description = "Test the method bindings generated, includes scenarios for static and instance fields.")
    public void methodMapping() throws FormatterException, ClassNotFoundException, BindgenException, IOException {
        Path methodFilePath = Paths.get(resourceDirectory.toString(), "unit-test-resources", "methods.bal");
        String methods = Files.readString(resourceDirectory.resolve(methodFilePath));
        SyntaxTree mSyntaxTree = getBindingsGenerator().generate(new JClass(this.getClass().getClassLoader()
                .loadClass("org.ballerinalang.bindgen.MethodsTestResource"), getBindgenEnv()));
        Assert.assertEquals(Formatter.format(mSyntaxTree.toSourceCode()), Formatter.format(methods));
        Assert.assertFalse(mSyntaxTree.hasDiagnostics());
    }

    @Test(description = "Test the field bindings generated, includes scenarios for static and instance fields.")
    public void fieldMapping() throws FormatterException, ClassNotFoundException, BindgenException, IOException {
        Path fieldFilePath = Paths.get(resourceDirectory.toString(), "unit-test-resources", "fields.bal");
        String fields = Files.readString(resourceDirectory.resolve(fieldFilePath));
        SyntaxTree fSyntaxTree = getBindingsGenerator().generate(new JClass(this.getClass().getClassLoader()
                .loadClass("org.ballerinalang.bindgen.FieldsTestResource"), getBindgenEnv()));
        Assert.assertEquals(Formatter.format(fSyntaxTree.toSourceCode()), Formatter.format(fields));
        Assert.assertFalse(fSyntaxTree.hasDiagnostics());
    }

    @Test(description = "Test the error bindings generated for a throwable.")
    public void errorMapping() throws FormatterException, ClassNotFoundException, BindgenException, IOException {
        Path errorFilePath = Paths.get(resourceDirectory.toString(), "unit-test-resources", "error.bal");
        String error = Files.readString(resourceDirectory.resolve(errorFilePath));
        SyntaxTree eSyntaxTree = getBindingsGenerator().generate(new JError(this.getClass().getClassLoader()
                .loadClass("java.io.IOException")));
        Assert.assertEquals(Formatter.format(eSyntaxTree.toSourceCode()), Formatter.format(error));
        Assert.assertFalse(eSyntaxTree.hasDiagnostics());
    }

    @Test(description = "Test the bindings generated for Java inner classes.")
    public void innerClassMapping() throws FormatterException, ClassNotFoundException, BindgenException, IOException {
        Path innerClassFilePath = Paths.get(resourceDirectory.toString(), "unit-test-resources", "innerClass.bal");
        String innerClass = Files.readString(resourceDirectory.resolve(innerClassFilePath));
        SyntaxTree iSyntaxTree = getBindingsGenerator().generate(new JClass(this.getClass().getClassLoader()
                .loadClass("java.lang.Character$Subset"), getBindgenEnv()));
        Assert.assertEquals(Formatter.format(iSyntaxTree.toSourceCode()), Formatter.format(innerClass));
        Assert.assertFalse(iSyntaxTree.hasDiagnostics());
    }

    @Test(description = "Test the bindings generated for a module level mapping.")
    public void moduleLevelMapping() throws FormatterException, ClassNotFoundException, BindgenException, IOException {
        BindgenEnv moduleBindgenEnv = new BindgenEnv();
        moduleBindgenEnv.setDirectJavaClass(true);
        moduleBindgenEnv.setModulesFlag(true);
        moduleBindgenEnv.setPackageName("test");
        moduleBindgenEnv.setPublicFlag(true);
        BindgenFileGenerator moduleBindingsGenerator = new BindgenFileGenerator(moduleBindgenEnv);

        Path moduleMappingPath = Paths.get(resourceDirectory.toString(), "unit-test-resources", "moduleMapping.bal");
        String moduleMappingValue = Files.readString(resourceDirectory.resolve(moduleMappingPath));
        SyntaxTree moduleSyntaxTree = moduleBindingsGenerator.generate(new JClass(this.getClass().getClassLoader()
                .loadClass("java.io.FileInputStream"), moduleBindgenEnv));
        Assert.assertEquals(Formatter.format(moduleSyntaxTree.toSourceCode()), Formatter.format(moduleMappingValue));
        Assert.assertFalse(moduleSyntaxTree.hasDiagnostics());
    }

    @Test(description = "Test the bindings generated for a direct throwable class mapping.")
    public void directThrowableMapping() throws FormatterException, ClassNotFoundException,
            BindgenException, IOException {
        BindgenEnv throwableBindgenEnv = getBindgenEnv();
        BindgenFileGenerator throwableBindingsGenerator = new BindgenFileGenerator(throwableBindgenEnv);
        Path throwableMappingPath = Paths.get(resourceDirectory.toString(),
                "unit-test-resources", "throwableMapping.bal");
        String throwableMappingValue = Files.readString(resourceDirectory.resolve(throwableMappingPath));
        SyntaxTree throwableSyntaxTree = throwableBindingsGenerator.generate(new JClass(this.getClass().getClassLoader()
                .loadClass("java.io.IOException"), throwableBindgenEnv));
        Assert.assertEquals(Formatter.format(throwableSyntaxTree.toSourceCode()),
                Formatter.format(throwableMappingValue));
        Assert.assertFalse(throwableSyntaxTree.hasDiagnostics());
    }

    private BindgenEnv getBindgenEnv() {
        BindgenEnv bindgenEnv = new BindgenEnv();
        bindgenEnv.setDirectJavaClass(true);
        return bindgenEnv;
    }

    private BindgenFileGenerator getBindingsGenerator() {
        return new BindgenFileGenerator(getBindgenEnv());
    }
}
