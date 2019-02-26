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
i is an int

type ::= int | boolean | string

returntype ::= type | void

op ::= + | - | * | /

*/EDITTED FOR NON AMBIGUITY
exp ::= additive | primary
 
additive ::= multiplicative ( ('+' | '-') multiplicative)*

multiplicative ::= primary ( ('*' | '/') primary)*

primary ::= i | var 
 */
 
vardec ::= type var

stmnt ::= vardec; |
          var = exp; |
          while(exp) stmnt |
          break; |
          str |
          type var |
          this |
          new classname(exp*) |
          println(exp) |
          { stmnt* } |
          if (exp) stmnt else stmnt |
          return exp; |
          return; |
          var.methodname(exp*)

access ::= public | private

methoddef ::= access returntype methodname(vardec*) stmnt

instancedec ::= access vardec;

classdef ::= access class classname [extends classname] {
             instancedec*
             constructor(vardec*) stmt
             methoddef*
             }
```
