# j2script

## Team Members:
```
Armine Khachatryan
Areeba Waheed
Robert Bedrosian
John Brehm
```

## The grammar:
```
var is a variable name
str is a string
i is an int

type ::= int | 
         boolean | 
         string | 
         classname // introduce wrapper classtype implements type which has a name

returntype ::= type | void

op ::= + | - | * | /

// might want to change.  Look at java grammar documentation
exp ::= additive |
        new classname(exp*) |
        var.methodname(exp*) | // will need to check if void
        str | 
        this
 
additive ::= multiplicative ( ('+' | '-') multiplicative)*

multiplicative ::= primary ( ('*' | '/') primary)*

primary ::= i | var 
 
vardec ::= type var

stmnt ::= exp; |
	      return exp; |
          return;
          break; |  
          println(exp); |
          { stmnt* } |
          vardec = exp; |
          var = exp; |
          if (exp) stmnt else stmnt |
          while(exp) stmnt
 
access ::= public | private

methoddef ::= access returntype methodname(vardec*) stmnt  // comma seperated vardecs

instancedec ::= access vardec;

// can’t access instance variables with dot.  Need getters and setters.
classdef ::= class classname [extends classname] {
             instancedec*
             constructor(vardec*) stmt
             methoddef*
             }

program ::= classdef* stmnt
```
