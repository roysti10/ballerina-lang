// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

string aString = "foo";
int anInt = 10;

function test() {
    string greet = "Hello " + aString;

    var greetFn = function (string name) returns string => HELLO + " " + name;
}

const HELLO = "Hello";

type Person record {|
    string name;
|};

type PersonObj object {
    string name;

    function getName() returns string;
};

class PersonClass {
    string name;

    function init(string name) {
        self.name = name;
    }

    function getName() returns string => self.name;
}

const RED = "red";
const GREEN = "green";
const BLUE = "blue";

type Colour RED|GREEN|BLUE;

function workers() {
    worker w1 {
        int x = 10;
    }
}

function typeNarrowing() {
    int|float|string val = 10;
    typedesc<anydata> td;

    if (val is int|float) {
        _ = val;

        if (val is int) {
            int x = val;
        } else {
            float f = val;
        }
    } else {
        string s = val;
    }
}

function exprBodyScope(string myStr) returns string => myStr;

public type Module table<TypeDef> key(id);

type TypeDef record {|
    readonly int id;
    string name;
    int cycleDepth = -1;
|};


function typeDes() {
    Module mod = table [
        {id: 1, name: "Mary"},
        {id: 2, name: "John",  cycleDepth: 25}
    ];
}

# Description
#
# + abc - Field Description of abc
public type Coord record {
    readonly int abc;
    # + y - Field Description
    int y;
};


# Description
#
# + name - Field Description of name
public type Pet object {
    string name;

    public function getName() returns string;

    public function kind() returns string;
};

# Represents a customer object.
#
# + name - Name of the customer
# + age - Age of the customer in years
# + address - Address of the customer
# + wealth - Account balance of the customer
public class Customer {
    public string name = "";
    public int age = 0;
    public string address = "";
    public float wealth = 0;

    # Gets invoked to initialize the `Customer` object.
    #
    # + name - Name of the customer for the constructor
    # + age - Age of the customer for the constructor
    public function init(string name, int age) {
    }

    # Get the address of the customer.
    #
    # + return - New address of the customer
    public function getAddress() returns string {
        return self.address ;
    }

    # Add the wealth of the customer.
    #
    # + amt - Amount to be added
    # + rate - Interest rate
    public function addWealth(int[] amt, float rate=1.5) {
    }
}

# Adds parameter `x` and parameter `y`
# + x - one thing to be added
# + y - another thing to be added
# + return - the sum of them
function add (int x, int y) returns int {
    return x + y;
}

# Calculates the value of the 'a' raised to the power of 'b'.
# ```ballerina
# float aPowerB = math:pow(3.2, 2.4);
# ```
#
# + a - Base value
# + b - Exponential value
# + return - Calculated exponential value
public isolated function pow(float a, float b) returns float {
    return 0;
}
