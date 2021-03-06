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
    private int minValue;
    private int maxValue;
    private int ownValue;
    private int nextChildRow;
    private int nextChildColumn;
    private int depth;
    private int choiceRow;
    private int choiceColumn;
    private int moves;
    private boolean solved;

    public Node(char[][] board, boolean isMax) {
        this(board, isMax, 0, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    public Node(char[][] board, boolean isMax, int moves, int choiceRow, int choiceColumn,
            int minValue,
            int maxValue, int depth) {
        this.board = board;
        this.isMax = isMax;
        this.depth = depth;
        this.moves = moves;
        this.choiceRow = choiceRow;
        this.choiceColumn = choiceColumn;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.solved = false;
        setValue();
    }

    private void setValue() {
        int value = generateValue();
        ownValue = value;
        if (isMax) {
            minValue = value;
        } else {
            maxValue = value;
        }

        nextChildColumn = 0;
        nextChildRow = 0;
    }

    private Node getNextChild() {
        while (nextChildRow < board.length && nextChildColumn < board.length) {
            if (board[nextChildRow][nextChildColumn] == '_') {
                board[nextChildRow][nextChildColumn] = (isMax) ? 'X' : 'O';

                Node result = new Node(board, !isMax, moves + 1, nextChildRow, nextChildColumn,
                        minValue,
                        maxValue, depth + 1);

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
        result.setMoves(moves + 1);
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MAX_VALUE;
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
        boolean setValue = false;
        if (node.isMax()) {
            for (Node child = node.getNextChild(); child != null; child = node.getNextChild()) {
                Node resultChild = minMaxAlgorithm(child, false);
                node.setOwnValue(Math.max(node.getOwnValue(), resultChild.getOwnValue()));
                if (node.getOwnValue() > node.getMinValue()) {
                    node.setMinValue(node.getOwnValue());
                    if (isRoot) {
                        node.setChoiceColumn(child.getChoiceColumn());
                        node.setChoiceRow(child.getChoiceRow());
                        setValue = true;
                    }
                }
                if (!setValue && isRoot) {
                    node.setChoiceColumn(child.getChoiceColumn());
                    node.setChoiceRow(child.getChoiceRow());
                }

                if (node.getMinValue() >= node.getMaxValue()) {
                    break;
                }
            }
        } else {
            for (Node child = node.getNextChild(); child != null; child = node.getNextChild()) {
                Node resultChild = minMaxAlgorithm(child, false);
                node.setOwnValue(Math.min(node.getOwnValue(), resultChild.getOwnValue()));
                if (node.getMaxValue() > node.getOwnValue()) {
                    node.setMaxValue(node.getOwnValue());
                    if (isRoot) {
                        node.setChoiceColumn(child.getChoiceColumn());
                        node.setChoiceRow(child.getChoiceRow());
                        setValue = true;
                    }
                }
                if (!setValue && isRoot) {
                    node.setChoiceColumn(child.getChoiceColumn());
                    node.setChoiceRow(child.getChoiceRow());
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
        moves++;
    }


    private int generateValue() {
        solved = true;
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
        if (moves < 9) {
            solved = false;
            return isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
        return 0;
    }

    public boolean checkIfSolved() {
        int value = generateValue();
        return !(value == Integer.MIN_VALUE || value == Integer.MAX_VALUE);
    }
}
