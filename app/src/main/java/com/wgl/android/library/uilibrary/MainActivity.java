package com.wgl.android.library.uilibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wgl.android.library.qfalertcontroller.QFAlertAction;
import com.wgl.android.library.qfalertcontroller.QFAlertController;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private String[] titles = new String[]{
            "Alert",
            "ActionSheet"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles));
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showAlert();
                break;
            case 1:
                showActionSheet();
                break;
            default:
                break;
        }
    }

    private void showAlert() {
        QFAlertController alertController = new QFAlertController(this, "我是标题", "我是很长很长很长很长很长很长很长很长很长很长很长很长的消息", QFAlertController.AlertControllerStyle.Alert);
        // 不管添加顺序怎样，AlertActionStyle.Cancel始终是在最底部的,AlertActionStyle.Default和AlertActionStyle.Destructive按添加的先后顺序显示
        alertController.addAction(new QFAlertAction("取消", QFAlertAction.AlertActionStyle.Cancel, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了取消");
            }
        }));
        alertController.addAction(new QFAlertAction("其他1", QFAlertAction.AlertActionStyle.Default, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了其他1");
            }
        }));
        alertController.addAction(new QFAlertAction("其他2", QFAlertAction.AlertActionStyle.Default, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了其他2");
            }
        }));
        alertController.addAction(new QFAlertAction("确定", QFAlertAction.AlertActionStyle.Default, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了确定");
            }
        }));
        alertController.show();
    }

    private void showActionSheet() {
        QFAlertController alertController = new QFAlertController(this, "我是标题", "我是很长很长很长很长很长很长很长很长很长很长很长很长的消息", QFAlertController.AlertControllerStyle.ActionSheet);
        // 不管添加顺序怎样，AlertActionStyle.Cancel始终是在最底部的,AlertActionStyle.Default和AlertActionStyle.Destructive按添加的先后顺序显示
        alertController.addAction(new QFAlertAction("取消", QFAlertAction.AlertActionStyle.Cancel, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了取消");
            }
        }));
        alertController.addAction(new QFAlertAction("其他1", QFAlertAction.AlertActionStyle.Default, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了其他1");
            }
        }));
        alertController.addAction(new QFAlertAction("其他2", QFAlertAction.AlertActionStyle.Default, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了其他2");
            }
        }));
        alertController.addAction(new QFAlertAction("确定", QFAlertAction.AlertActionStyle.Destructive, new QFAlertAction.Delegate() {
            @Override
            public void onClick() {
                showToast("点击了确定");
            }
        }));
        alertController.show();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
