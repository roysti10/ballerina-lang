/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package io.ballerinalang.compiler.internal.diagnostics;

import io.ballerinalang.compiler.diagnostics.DiagnosticSeverity;

/**
 * Represents a diagnostic error code.
 *
 * @since 2.0.0
 */
public enum DiagnosticErrorCode implements DiagnosticCode {
    // TODO figure out an order of these error codes
    // Tokens
    ERROR_MISSING_TOKEN("BCE0001"),
    ERROR_MISSING_SEMICOLON_TOKEN("BCE0002"),
    ERROR_MISSING_COLON_TOKEN("BCE0003"),
    ERROR_MISSING_OPEN_PAREN_TOKEN("BCE0004"),
    ERROR_MISSING_CLOSE_PAREN_TOKEN("BCE0005"),
    ERROR_MISSING_OPEN_BRACE_TOKEN("BCE0006"),
    ERROR_MISSING_CLOSE_BRACE_TOKEN("BCE0007"),
    ERROR_MISSING_OPEN_BRACKET_TOKEN("BCE0008"),
    ERROR_MISSING_CLOSE_BRACKET_TOKEN("BCE0009"),
    ERROR_MISSING_EQUAL_TOKEN("BCE0010"),
    ERROR_MISSING_COMMA_TOKEN("BCE00011"),
    ERROR_MISSING_PLUS_TOKEN("BCE00012"),
    ERROR_MISSING_SLASH_TOKEN("BCE00013"),
    ERROR_MISSING_AT_TOKEN("BCE00014"),
    ERROR_MISSING_QUESTION_MARK_TOKEN("BCE00015"),
    ERROR_MISSING_GT_TOKEN("BCE00016"),
    ERROR_MISSING_GT_EQUAL_TOKEN("BCE00017"),
    ERROR_MISSING_LT_TOKEN("BCE00018"),
    ERROR_MISSING_LT_EQUAL_TOKEN("BCE00019"),
    ERROR_MISSING_RIGHT_DOUBLE_ARROW_TOKEN("BCE00020"),
    ERROR_MISSING_XML_COMMENT_END_TOKEN("BCE00021"),
    ERROR_MISSING_XML_PI_END_TOKEN("BCE00022"),
    ERROR_MISSING_DOUBLE_QUOTE_TOKEN("BCE00023"),
    ERROR_MISSING_BACKTICK_TOKEN("BCE00024"),
    ERROR_MISSING_OPEN_BRACE_PIPE_TOKEN("BCE00025"),
    ERROR_MISSING_CLOSE_BRACE_PIPE_TOKEN("BCE00026"),
    ERROR_MISSING_ASTERISK_TOKEN("BCE00027"),
    ERROR_MISSING_PIPE_TOKEN("BCE00028"),

    // Keywords
    ERROR_MISSING_DEFAULT_KEYWORD("BCE00030"),
    ERROR_MISSING_TYPE_KEYWORD("BCE00031"),
    ERROR_MISSING_ON_KEYWORD("BCE00032"),
    ERROR_MISSING_ANNOTATION_KEYWORD("BCE00033"),
    ERROR_MISSING_FUNCTION_KEYWORD("BCE00034"),
    ERROR_MISSING_SOURCE_KEYWORD("BCE00035"),
    ERROR_MISSING_ENUM_KEYWORD("BCE00036"),
    ERROR_MISSING_FIELD_KEYWORD("BCE00037"),
    ERROR_MISSING_VERSION_KEYWORD("BCE00038"),
    ERROR_MISSING_OBJECT_KEYWORD("BCE00039"),
    ERROR_MISSING_RECORD_KEYWORD("BCE00040"),
    ERROR_MISSING_SERVICE_KEYWORD("BCE00041"),
    ERROR_MISSING_AS_KEYWORD("BCE00042"),
    ERROR_MISSING_LET_KEYWORD("BCE00043"),
    ERROR_MISSING_TABLE_KEYWORD("BCE00044"),
    ERROR_MISSING_KEY_KEYWORD("BCE00045"),
    ERROR_MISSING_FROM_KEYWORD("BCE00046"),
    ERROR_MISSING_IN_KEYWORD("BCE00046"),
    ERROR_MISSING_IF_KEYWORD("BCE00047"),
    ERROR_MISSING_IMPORT_KEYWORD("BCE00048"),
    ERROR_MISSING_EXTERNAL_KEYWORD("BCE00049"),

    ERROR_MISSING_IDENTIFIER("BCE0050"),
    ERROR_MISSING_DECIMAL_INTEGER_LITERAL("BCE0051"),

    ERROR_MISSING_FUNCTION_NAME("BCE0060"),

    ERROR_MISSING_TYPE_DESC("BCE0100"),
    ERROR_MISSING_EXPRESSION("BCE0101"),
    ERROR_MISSING_SELECT_CLAUSE("BCE0102"),

    ERROR_MISSING_ANNOTATION_ATTACH_POINT("BCE200"),
    ERROR_MISSING_LET_VARIABLE_DECLARATION("BCE201"),
    ERROR_MISSING_NAMED_WORKER_DECLARATION("BCE202"),
    ;

    String diagnosticId;

    DiagnosticErrorCode(String diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    @Override
    public DiagnosticSeverity severity() {
        return DiagnosticSeverity.ERROR;
    }

    @Override
    public String diagnosticId() {
        return diagnosticId;
    }
}
