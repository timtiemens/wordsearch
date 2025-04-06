<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WordSearch</title>
    </head>
    <body>
        <#list listoffilenames as filename>
           <li style="text-align: left"> <a href="/game?fileName=${ filename }">${ filename }</a></li></li>
        </#list>
    </body>
</html>
