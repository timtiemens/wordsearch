<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WordSearch Game Display</title>
        <script>
          function myFunction() {
            alert('Span was clicked!');
          }
          function createId(row, col) {
            return "r" + row + "c" + col
          }
        </script>
    </head>
    <body>
        WordSearch game.

        <p>Number of rows is ${gameModel.getRows()}</p>
        <p>Number of cols is ${gameModel.getCols()}</p>

        <table>
        <#list gameModel.getRowsNumberList() as row>
          <tr>
            <#list gameModel.getColsNumberList() as col>
               <#assign idval = 'r1c1'>
               <td id="r${row}c${col}" style="min-width: 5px; text-align: center;"> ${ gameModel.getGrid().getAt(row, col) }  </td>
            </#list>
          </tr>
        </#list>
        </table>

         <ul>
          <#list  gameModel.getAnswers().getAnswerWordString() as word>
             <li style="text-align: left">
               <span onclick="myFunction()">${ word } ${ gameModel.getAnswers().getAnswerWordRecordForWord(word).direction() }</span>
             </li>
          </#list>
        </ul>

        <ul>
          <#list  gameModel.getLookForWords().getLookFor() as word>
             <li style="text-align: left"><span onclick="myFunction()">${ word }</span>    </li>
          </#list>
        </ul>
    </body>
</html>
