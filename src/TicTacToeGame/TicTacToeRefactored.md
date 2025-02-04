Первоначальный вариант:
```java
public class TicTacToeClient extends Application
        implements TicTacToeConstants {


    private boolean myTurn = false;
    private char myToken = ' ';
    private char otherToken = ' ';
    private Cell[][] cell =  new Cell[3][3];
    private Label lblTitle = new Label();
    private Label lblStatus = new Label();


    private int rowSelected;
    private int columnSelected;

    private DataInputStream fromServer;
    private DataOutputStream toServer;


    private boolean continueToPlay = true;

    private boolean waiting = true;

    private String host = "localhost";

    @Override 
    public void start(Stage primaryStage) {

        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                pane.add(cell[i][j] = new Cell(i, j), j, i);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(lblTitle);
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        Scene scene = new Scene(borderPane, 320, 350);
        primaryStage.setTitle("TicTacToeClient"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        connectToServer();
    }

    private void connectToServer() {}
    // the rest of the code
}
```
Вариант в функциональном стиле:

Цепочка объявлений свойств класса TicTacToeClient собрана в отдельном классе GameState 
c методами updatedTurn, updatedCell, waiting, которые возвращают новый экземпляр этого класса (иммутабельность). 
```java
public class TicTacToeClient2 extends Application implements TicTacToeConstants {


    public static final class GameState {
        private final boolean myTurn;
        private final char myToken;
        private final char otherToken;
        private final boolean continueToPlay;
        private final boolean waiting;
        private final int rowSelected;
        private final int columnSelected;
        private final Cell[][] cell;

        public GameState(boolean myTurn, char myToken, char otherToken,
                         boolean continueToPlay, boolean waiting,
                         int rowSelected, int columnSelected, Cell[][] cell) {
            this.myTurn = myTurn;
            this.myToken = myToken;
            this.otherToken = otherToken;
            this.continueToPlay = continueToPlay;
            this.waiting = waiting;
            this.rowSelected = rowSelected;
            this.columnSelected = columnSelected;
            this.cell = cell;
        }


        public GameState withUpdatedTurn(boolean newTurn) {
            return new GameState(newTurn, myToken, otherToken, continueToPlay, waiting, rowSelected, columnSelected, cell);
        }

        public GameState withUpdatedCell(int row, int col, char token) {
            Cell[][] newCells = copyCells(cell);
            newCells[row][col].setToken(token);
            return new GameState(myTurn, myToken, otherToken, continueToPlay, waiting, row, col, newCells);
        }

        public GameState withWaiting(boolean waiting) {
            return new GameState(myTurn, myToken, otherToken, continueToPlay, waiting, rowSelected, columnSelected, cell);
        }

        private static Cell[][] copyCells(Cell[][] originalCells) {
            Cell[][] newCells = new Cell[originalCells.length][originalCells[0].length];
            for (int i = 0; i < originalCells.length; i++) {
                for (int j = 0; j < originalCells[i].length; j++) {
                    newCells[i][j] = new Cell(originalCells[i][j].getRow(), originalCells[i][j].getColumn(), originalCells[i][j].getToken());
                }
            }
            return newCells;
        }
    }
// the rest of the code
```
