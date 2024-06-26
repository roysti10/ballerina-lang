/*
 *  Copyright (c) 2020, WSO2 LLC. (http://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package io.ballerina.compiler.internal.parser.tree;

import io.ballerina.compiler.syntax.tree.ForEachStatementNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NonTerminalNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;

import java.util.Collection;
import java.util.Collections;

/**
 * This is a generated internal syntax tree node.
 *
 * @since 2.0.0
 */
public class STForEachStatementNode extends STStatementNode {
    public final STNode forEachKeyword;
    public final STNode typedBindingPattern;
    public final STNode inKeyword;
    public final STNode actionOrExpressionNode;
    public final STNode blockStatement;
    public final STNode onFailClause;

    STForEachStatementNode(
            STNode forEachKeyword,
            STNode typedBindingPattern,
            STNode inKeyword,
            STNode actionOrExpressionNode,
            STNode blockStatement,
            STNode onFailClause) {
        this(
                forEachKeyword,
                typedBindingPattern,
                inKeyword,
                actionOrExpressionNode,
                blockStatement,
                onFailClause,
                Collections.emptyList());
    }

    STForEachStatementNode(
            STNode forEachKeyword,
            STNode typedBindingPattern,
            STNode inKeyword,
            STNode actionOrExpressionNode,
            STNode blockStatement,
            STNode onFailClause,
            Collection<STNodeDiagnostic> diagnostics) {
        super(SyntaxKind.FOREACH_STATEMENT, diagnostics);
        this.forEachKeyword = forEachKeyword;
        this.typedBindingPattern = typedBindingPattern;
        this.inKeyword = inKeyword;
        this.actionOrExpressionNode = actionOrExpressionNode;
        this.blockStatement = blockStatement;
        this.onFailClause = onFailClause;

        addChildren(
                forEachKeyword,
                typedBindingPattern,
                inKeyword,
                actionOrExpressionNode,
                blockStatement,
                onFailClause);
    }

    @Override
    public STNode modifyWith(Collection<STNodeDiagnostic> diagnostics) {
        return new STForEachStatementNode(
                this.forEachKeyword,
                this.typedBindingPattern,
                this.inKeyword,
                this.actionOrExpressionNode,
                this.blockStatement,
                this.onFailClause,
                diagnostics);
    }

    public STForEachStatementNode modify(
            STNode forEachKeyword,
            STNode typedBindingPattern,
            STNode inKeyword,
            STNode actionOrExpressionNode,
            STNode blockStatement,
            STNode onFailClause) {
        if (checkForReferenceEquality(
                forEachKeyword,
                typedBindingPattern,
                inKeyword,
                actionOrExpressionNode,
                blockStatement,
                onFailClause)) {
            return this;
        }

        return new STForEachStatementNode(
                forEachKeyword,
                typedBindingPattern,
                inKeyword,
                actionOrExpressionNode,
                blockStatement,
                onFailClause,
                diagnostics);
    }

    @Override
    public Node createFacade(int position, NonTerminalNode parent) {
        return new ForEachStatementNode(this, position, parent);
    }

    @Override
    public void accept(STNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T apply(STNodeTransformer<T> transformer) {
        return transformer.transform(this);
    }
}
