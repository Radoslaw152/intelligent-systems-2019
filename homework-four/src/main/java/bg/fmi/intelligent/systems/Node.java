package bg.fmi.intelligent.systems;

import java.util.Arrays;
import java.util.Scanner;

import lombok.Getter;

@Getter
public class Node {
    private char[][] board;
    private boolean isMax;
    private int minValue = -10;
    private int maxValue = 10;
    private int ownValue;
    private int nextChildRow = 0;
    private int nextChildColumn = 0;
    private static final int NOT_SOLVED = -100;
    private int level;
    private Node parent;

    public Node(char[][] board, boolean isMax) {
        this(board, isMax, 1, null);
    }

    public Node(char[][] board, boolean isMax, int level, Node parent) {
        this.board = board;
        this.isMax = isMax;
        this.level = level;
        this.parent = parent;
        if (this.level < 5) {
            this.ownValue = NOT_SOLVED;
        } else {
            this.ownValue = generateValue();
            if (ownValue != NOT_SOLVED) {
                if (isMax) {
                    minValue = ownValue;
                } else {
                    maxValue = ownValue;
                }
            }
        }

    }

    private Node getNextChild() {
        while (nextChildRow < board.length && nextChildColumn < board.length) {
            if (board[nextChildRow][nextChildColumn] == '_') {
                char[][] childBoard = Arrays.stream(board)
                        .map(char[]::clone)
                        .toArray(char[][]::new);
                childBoard[nextChildRow][nextChildColumn] = (isMax) ? 'X' : 'O';
                return new Node(childBoard, !isMax, level + 1, this);
            }
            nextChildColumn++;
            if (nextChildColumn == board.length) {
                nextChildRow++;
                nextChildColumn = 0;
            }
        }
        return null;
    }

    public Node traverse() {
        if (ownValue != NOT_SOLVED || foundValue()) {
            return this;
        }
        for (Node temp = getNextChild(); temp != null; temp = getNextChild()) {
            if (parent != null) {
                if (isMax && parent.getMaxValue() < minValue) {
                    return this;
                } else if (!isMax && parent.getMinValue() > maxValue) {
                    return this;
                }
            }
            Node childChosen = temp.traverse();
            int childValue = childChosen.getValue();
            if (isMax && minValue < childValue) {
                minValue = childValue;
            } else if (!isMax && maxValue > childValue){
                maxValue = childValue;
            }
            if (foundValue()) {
                if(parent == null) {
                    this.board = childChosen.getBoard();
                    this.nextChildRow = 0;
                    this.nextChildColumn = 0;
                }
                return this;
            }
        }
        return this;
    }

    public void setValueInBoard(Scanner scanner) {
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        while (board[row][column] != '_') {
            System.out.println("Please type again. ");
            row = scanner.nextInt();
            column = scanner.nextInt();
        }
        board[row][column] = (isMax) ? 'O' : 'X';
        level++;
    }

    public boolean foundValue() {
        return minValue == maxValue;
    }

    private int getValue() {
        return (isMax) ? minValue : maxValue;
    }

    private int generateValue() {
        for (int i = 0; i < board.length; ++i) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] != '_') {
                return (board[i][0] == 'X') ? 10 : -10;
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] != '_') {
                return (board[i][0] == 'X') ? 10 : -10;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] != '_') {
            return (board[1][1] == 'X') ? 10 : -10;
        }
        if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[1][1] != '_') {
            return (board[1][1] == 'X') ? 10 : -10;
        }
        for (char[] chars : board) {
            for (int j = 0; j < board.length; ++j) {
                if (chars[j] == '_') {
                    return NOT_SOLVED;
                }
            }
        }
        return 0;
    }
}
