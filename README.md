# j2script

## Team Members:
```
Armine Khachatryan
Areeba Waheed
Robert Bedrosian
John Brehm
Carlos Sandoval
```

## The grammar:
```
var is a variable name
str is a string

type ::= int | boolean | void | classname
op ::= + | - | * | /
*/EDITTED FOR NON AMBIGUITY
exp ::= additive |
 primary.methodname(exp*) |
 primary
additive ::= multiplicative ( ('+' | '-') multiplicative)*
multiplicative ::= primary ( ('*' | '/') primary)*
primary ::= type var |
 str |
 this |
 println(exp) |
 new classname(exp*)
 */
vardec ::= type var
stmnt ::= vardec; |
       var = exp; |
       while(exp) stmnt |
       break; |
       { stmnt* } |
       if (exp) stmnt else stmnt |
       return exp; |
       return;
access ::= public | private
methoddef ::= access type methodname(vardec*) stmnt
instancedec ::= access vardec;
classdef ::= access class classname [extends classname] {
        instancedec*
        constructor(vardec*) stmt
        methoddef*
        }
```
