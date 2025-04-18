<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>WordSearch Game Display</title>
        <script>
          function myFunction(word, listrowcol) {
            aslist = listrowcol.split(",");
            setHighLight(word, aslist);
          }

          function setHighLight(word, aslist) {
            color = 'red'
            aslist.forEach(rc => {
                const element = document.getElementById(rc);
                if (element != null) {
                    element.style.backgroundColor = color; // Set background color to red
                    color = 'green'
                } else {
                    console.log("Did not find element " + rc);
                }
            });
          }

          function highlightAllWords() {
             const myMap = getAllWordAndListRowCol();

             for (const [key, value] of myMap) {
                console.log(key)
                console.log(value['rowcollist'])
                setHighLight( key, value['rowcollist'].split(",") )
             }
          }

          function getAllWordAndListRowCol() {
            ret = new Map();

            <#list  gameModel.getAnswers().getAnswerWordString() as word>
               <#assign rowcollist = '${ gameModel.getAnswers().getAnswerWordRecordForWord(word).getStringForRowColList() }'>
               item = {  'rowcollist': '${rowcollist}',
                         'direction' : '${ gameModel.getAnswers().getAnswerWordRecordForWord(word).direction() }'
                      }
               ret.set( '${ word }', item )
            </#list>
            console.log("GetAllWordAndListRowCol, size=" + ret.size)
            return ret
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
               <td id="r${row}c${col}" style="min-width: 5px; text-align: center;"> ${ gameModel.getGrid().getAt(row, col) }  </td>
            </#list>
          </tr>
        </#list>
        </table>

        <button onclick="highlightAllWords()">Highlight All</button>

         <ul>
          <#list  gameModel.getAnswers().getAnswerWordString() as word>
             <li style="text-align: left">
               <#assign rowcollist = '${ gameModel.getAnswers().getAnswerWordRecordForWord(word).getStringForRowColList() }'>
               <span onclick="myFunction('${ word }', '${rowcollist}')">${ word } ${ gameModel.getAnswers().getAnswerWordRecordForWord(word).direction() }</span>
             </li>
          </#list>
        </ul>

    </body>
</html>
