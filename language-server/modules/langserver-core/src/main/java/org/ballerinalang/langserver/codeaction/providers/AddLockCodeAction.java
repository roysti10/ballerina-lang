/*
 *  Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com)
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.langserver.codeaction.providers;

import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.StatementNode;
import io.ballerina.tools.diagnostics.Diagnostic;
import io.ballerina.tools.text.LinePosition;
import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.codeaction.CodeActionNodeValidator;
import org.ballerinalang.langserver.codeaction.CodeActionUtil;
import org.ballerinalang.langserver.common.constants.CommandConstants;
import org.ballerinalang.langserver.common.utils.CommonUtil;
import org.ballerinalang.langserver.commons.CodeActionContext;
import org.ballerinalang.langserver.commons.codeaction.spi.DiagBasedPositionDetails;
import org.ballerinalang.langserver.commons.codeaction.spi.DiagnosticBasedCodeActionProvider;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * An code action to wrap an isolated variable with a lock.
 *
 * @since 2201.9.0
 */
@JavaSPIService("org.ballerinalang.langserver.commons.codeaction.spi.LSCodeActionProvider")
public class AddLockCodeAction implements DiagnosticBasedCodeActionProvider {

    private static final String NAME = "Add lock";
    private static final Set<String> DIAGNOSTIC_CODES = Set.of("BCE3957", "BCE3962");

    @Override
    public boolean validate(Diagnostic diagnostic, DiagBasedPositionDetails positionDetails,
                            CodeActionContext context) {
        return DIAGNOSTIC_CODES.contains(diagnostic.diagnosticInfo().code())
                && CodeActionNodeValidator.validate(context.nodeAtRange());
    }

    @Override
    public List<CodeAction> getCodeActions(Diagnostic diagnostic, DiagBasedPositionDetails positionDetails,
                                           CodeActionContext context) {
        Optional<StatementNode> matchingStatementNode = getMatchingStatementNode(positionDetails.matchedNode());
        if (matchingStatementNode.isEmpty()) {
            return Collections.emptyList();
        }

        TextEdit surroundWithLockEditText = getSurroundWithLockEditText(matchingStatementNode.get());
        return Collections.singletonList(CodeActionUtil.createCodeAction(
                CommandConstants.SURROUND_WITH_LOCK,
                List.of(surroundWithLockEditText),
                context.fileUri(),
                CodeActionKind.QuickFix)
        );
    }

    private static Optional<StatementNode> getMatchingStatementNode(Node matchedNode) {
        Node parentNode = matchedNode.parent();
        while (parentNode != null && !(parentNode instanceof StatementNode)) {
            parentNode = parentNode.parent();
        }
        return Optional.ofNullable((StatementNode) parentNode);
    }

    private static TextEdit getSurroundWithLockEditText(Node node) {
        LinePosition startLinePosition = node.lineRange().startLine();
        LinePosition endLinePosition = node.lineRange().endLine();
        Position positionLock = new Position(startLinePosition.line(), startLinePosition.offset());
        Position posCheckLineStart = new Position(endLinePosition.line(), endLinePosition.offset());

        String spaces = " ".repeat(startLinePosition.offset());
        String editText = "lock {" + CommonUtil.LINE_SEPARATOR + "\t" + node.toSourceCode() + spaces + "}";
        return new TextEdit(new Range(positionLock, posCheckLineStart), editText);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
