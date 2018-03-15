[![License](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)
![JDK](https://img.shields.io/badge/jdk-9-yellowgreen.svg)

# PassGen

A simple Java command-line application to generate unique secure passwords based based on a website
or service name and an existing password or passphrase. Instead of using an ultimately forgettable
password or passphrase, the application can generate a password based on a series of optional security
questions.

The goal of this application is to limit the issue of password reuse without needing to store a 
list of passwords as a typical password manager does. A stored list of passwords is vulnerable to 
attack and possible publication, and a master password can be forgotten. By generating a password
using security questions, it is possible to avoid both of these problems. Passwords that are not 
stored cannot be revealed, and the answers to security questions are not likely to be forgotten. 

## Usage

The application can run in two modes. If command line arguments are passed at startup, the 
application will immediately generate a password based on those arguments. For example, the command:
```
$ passgen --max-length 15 correct horse battery staple
```

Will produce the following output:
```
Password: vs54IaUZ+1+OI9p
Copy to clipboard? [Y/N]
```

The `--max-length` switch sets a maximum length for the generated password. This switch should only
be used when a website or service explicitly sets an upper bound for password length.

Alternatively, running the application without any command-line arguments will set it to interactive 
mode wherein the application will prompt the user for the desired website name, a maximum 
password length, and three security questions. All but the website name question can be ignored, but
for the application to be effective, at least one security question should be answered. It is 
recommended to answer at least two. 

## Requirements

Java 9 is required to build and run the application.

## Downloads

Binary archives (complete with startup scripts) can be downloaded from the 'Releases' section 

## Building From Source

To build the application from source, clone this repository and, in the root directory, run

```
./gradlew build
```

This will build the application and create binary distributable archives in the 
`./console-application/build/distributions` directory.

## Feedback

Suggestions, bug reports, and pull requests are all welcome and appreciated. You are 
also welcome to send me an email at dev (AT) jmburns.xyz if you feel the need to do so.

## Licensing

Copyright 2018 Jacques Burns

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
Software, and to permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 