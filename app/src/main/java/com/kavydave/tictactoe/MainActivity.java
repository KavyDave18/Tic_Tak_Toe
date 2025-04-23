package com.kavydave.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // 0 - X, 1 - O, 2 - null
    boolean gameActive = true;
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int[][] winPosition = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 4, 8}, {2, 4, 6}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}};

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int clickedImg = Integer.parseInt(img.getTag().toString());

        if (!gameActive) {
            gameReset(view);
            return;
        }

        if (gameState[clickedImg] == 2) {
            gameState[clickedImg] = activePlayer;
            img.setTranslationY(-1000f);
            if (activePlayer == 0) {
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                updateStatus("O's Turn - Tap to Play");
            } else {
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                updateStatus("X's Turn - Tap to Play");
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }

        // Check for a winner
        for (int[] winpositions : winPosition) {
            if (gameState[winpositions[0]] == gameState[winpositions[1]] &&
                    gameState[winpositions[1]] == gameState[winpositions[2]] &&
                    gameState[winpositions[0]] != 2) {
                gameActive = false;

                // Determine the winner
                String winner = (gameState[winpositions[0]] == 0) ? "X has won" : "O has won";
                updateStatus(winner);
                return;
            }
        }

        // Check for a draw
        boolean draw = true;
        for (int state : gameState) {
            if (state == 2) {
                draw = false;
                break;
            }
        }
        if (draw) {
            gameActive = false;
            updateStatus("It's a Draw!");
        }
    }

    public void gameReset(View view) {
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }

        // Reset all ImageViews
        int[] imageIds = {
                R.id.imageView2, R.id.imageView3, R.id.imageView4,
                R.id.imageView5, R.id.imageView6, R.id.imageView7,
                R.id.imageView8, R.id.imageView9, R.id.imageView10
        };

        for (int id : imageIds) {
            ImageView img = findViewById(id);
            if (img != null) {
                img.setImageResource(0);
            }
        }

        // Reset status bar
        updateStatus("X's Turn - Tap to Play");
    }

    private void updateStatus(String message) {
        TextView status = findViewById(R.id.Status);
        status.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
