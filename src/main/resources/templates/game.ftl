<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WordSearch Game Display</title>
    </head>
    <body>
        Hello game.ftl ${title} ${title}


        <p>Number of rows is ${gameModel.getRows()}</p>
        <p>Number of cols is ${gameModel.getCols()}</p>

        <table>
        <#list gameModel.getRowsNumberList() as row>
          <tr>
            <#list gameModel.getColsNumberList() as col>
               <td style="min-width: 5px; text-align: center"> ${ gameModel.getAt(row, col) }</td>
            </#list>
          </tr>
        </#list>
        </table>

    </body>
</html>
