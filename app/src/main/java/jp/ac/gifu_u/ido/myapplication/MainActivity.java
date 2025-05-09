package jp.ac.gifu_u.ido.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 端の余白調整（EdgeToEdge）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 終了ボタン処理
        Button exitButton = findViewById(R.id.exitButton);
        if (exitButton != null) {
            exitButton.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "アプリを終了しました", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
