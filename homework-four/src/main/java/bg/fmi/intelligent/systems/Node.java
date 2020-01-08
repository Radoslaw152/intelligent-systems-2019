package bg.fmi.intelligent.systems;

import java.util.Arrays;
import java.util.Scanner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {
    private char[][] board;
    private boolean isMax;
    private boolean isSolved;
    private int minValue;
    private int maxValue;
    private int ownValue;
    private int nextChildRow;
    private int nextChildColumn;
    private int depth;
    private int choiceRow;
    private int choiceColumn;

    public Node(char[][] board, boolean isMax) {
        this(board, isMax, 0,0,0);
    }

    public Node(char[][] board, boolean isMax, int choiceRow, int choiceColumn,  int depth) {
        this.board = board;
        this.isMax = isMax;
        this.depth = depth;
        this.choiceRow = choiceRow;
        this.choiceColumn = choiceColumn;
        setValue();
    }

    private void setValue() {
        int value = generateValue();
        if (!isSolved) {
            if (isMax) {
                ownValue = Integer.MIN_VALUE;
            } else {
                ownValue = Integer.MAX_VALUE;
            }
        } else {
            ownValue = value;
            if (isMax) {
                minValue = value;
            } else {
                maxValue = value;
            }
        }
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MAX_VALUE;
        nextChildColumn = 0;
        nextChildRow = 0;
    }

    private Node getNextChild() {
        while (nextChildRow < board.length && nextChildColumn < board.length) {
            if (board[nextChildRow][nextChildColumn] == '_') {
                board[nextChildRow][nextChildColumn] = (isMax) ? 'X' : 'O';

                Node result = new Node(board, !isMax, nextChildRow, nextChildColumn, depth + 1);

                nextChildColumn++;
                if (nextChildColumn == board.length) {
                    nextChildRow++;
                    nextChildColumn = 0;
                }
                return result;
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
        char[][] tempBoard = Arrays.stream(board)
                .map(char[]::clone)
                .toArray(char[][]::new);

        this.setValue();
        Node result = minMaxAlgorithm(this, true);
        choiceRow = result.getChoiceRow();
        choiceColumn = result.getChoiceColumn();
        board = tempBoard;
        board[choiceRow][choiceColumn] = (isMax) ? 'X' : 'O';
        this.setValue();
        return this;
    }

    private void clearChoice() {
        board[choiceRow][choiceColumn] = '_';
    }

    private static Node minMaxAlgorithm(Node node, boolean isRoot) {
        if (node.isSolved()) {
            if (!isRoot) {
                node.clearChoice();
            }
            return node;
        }
        Node result;
        if (node.isMax()) {
            for (Node child = node.getNextChild(); child != null; child = node.getNextChild()) {
                int minValue = node.getMinValue();

                Node resultChild = minMaxAlgorithm(child, false);
//                if(result.)
                node.setOwnValue(Math.max(node.getOwnValue(), resultChild.getOwnValue()));
                node.setMinValue(Math.max(node.getOwnValue(), node.getMinValue()));

                if (node.getOwnValue() != Integer.MIN_VALUE && node.getMinValue() != minValue) {
                    node.setChoiceRow(child.getChoiceRow());
                    node.setChoiceColumn(child.getChoiceColumn());
                }

                if (node.getMinValue() >= node.getMaxValue()) {
                    break;
                }
            }
        } else {
            for (Node child = node.getNextChild(); child != null; child = node.getNextChild()) {
                int maxValue = node.getMaxValue();

                Node resultChild = minMaxAlgorithm(child, false);
                if(!node.isSolved) {
                    node.setOwnValue(resultChild.getOwnValue());
                }
//                node.setOwnValue(Math.min(node.getOwnValue(), resultChild.getOwnValue()));
                node.setMaxValue(Math.min(node.getOwnValue(), node.getMaxValue()));

                if (node.getOwnValue() != Integer.MAX_VALUE && node.getMinValue() != maxValue) {
                    node.setChoiceRow(child.getChoiceRow());
                    node.setChoiceColumn(child.getChoiceColumn());
                }
                if (node.getMinValue() >= node.getMaxValue()) {
                    break;
                }
            }
        }
        if (!isRoot) {
            node.clearChoice();
        }
        return node;
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
    }


    private int generateValue() {
        isSolved = true;
        for (int i = 0; i < board.length; ++i) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] != '_') {
                return (board[i][0] == 'X') ? 10 - depth : -10 + depth;
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] != '_') {
                return (board[i][0] == 'X') ? 10 - depth : -10 + depth;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] != '_') {
            return (board[1][1] == 'X') ? 10 - depth : -10 + depth;
        }
        if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[1][1] != '_') {
            return (board[1][1] == 'X') ? 10 - depth : -10 + depth;
        }
        isSolved = false;
        return 0;
    }
}
