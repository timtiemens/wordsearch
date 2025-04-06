<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WordSearch Game Display</title>
    </head>
    <body>
        WordSearch game.

        <p>Number of rows is ${gameModel.getRows()}</p>
        <p>Number of cols is ${gameModel.getCols()}</p>

        <table>
        <#list gameModel.getRowsNumberList() as row>
          <tr>
            <#list gameModel.getColsNumberList() as col>
               <td style="min-width: 5px; text-align: center"> ${ gameModel.getGrid().getAt(row, col) }</td>
            </#list>
          </tr>
        </#list>
        </table>

        <ul>
          <#list  gameModel.getLookForWords().getLookFor() as word>
             <li style="text-align: left">${ word }</li>
          </#list>
        </ul>
    </body>
</html>
