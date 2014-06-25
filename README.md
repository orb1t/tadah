<pre>
    ____        __       _       ___    
   / __ \____ _/ /_____ | |     / (_)___
  / / / / __ `/ __/ __ `/ | /| / / /_  /
 / /_/ / /_/ / /_/ /_/ /| |/ |/ / / / /_
/_____/\__,_/\__/\__,_/ |__/|__/_/ /___/

A test data preparation framework with Behavior-Driven flavor
</pre>                                     

Usage
=====

JUnit
-----

1. Annotate your test method with one of the following (it also works with combination of those)
 * TadahDataSpec: data spec phrase
 * TadahDataSpecList: list of data spec phrase
 * TadahDataSpecFile: define your own data spec file
2. Call DataSpecEngine in before and after test
