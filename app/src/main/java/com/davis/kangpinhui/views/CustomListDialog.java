package com.davis.kangpinhui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.davis.kangpinhui.R;
import com.davis.kangpinhui.adapter.base.CommonBaseAdapter;
import com.davis.kangpinhui.adapter.base.ViewHolder;

import java.util.ArrayList;


public class CustomListDialog {
    /**
     * 上下文
     */
    Context context;
    /**
     * 对话框
     */
    Dialog dialog;
    /**
     * 窗体
     */
    Window window;
    /**
     * 标题
     */
    TextView titleView;

    ListView dialog_list;

    OnItemClick onItemClick;


    /**
     * 创建dialog
     *
     * @param context
     */
    public CustomListDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.BankListDialog)/*new AlertDialog.Builder(context).create()*/;
        window = dialog.getWindow();
        dialog.show();
        window.setContentView(R.layout.layout_list_dialog);
        setWidth();
        titleView = (TextView) window.findViewById(R.id.title);
        dialog_list = (ListView) window.findViewById(R.id.dialog_list);
        dialog.setCanceledOnTouchOutside(false);

    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setList(CharSequence[] typepaytext){

        ArrayList<String> list=new ArrayList<>();
        for (int i = 0; i < typepaytext.length; i++) {
            list.add(typepaytext[i].toString());
        }
       dialog_list.setAdapter(new CommonBaseAdapter<String>(context,list,R.layout.layout_item_dialog) {
           @Override
           public void convert(ViewHolder holder, String itemData, int position) {
               holder.setText(R.id.dialog_list_item,itemData);
           }
       });

        dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onItemClick!=null){
                    onItemClick.click(position);
                }
                dialog.dismiss();
            }
        });
   }



    /**
     * 设置对话框的宽度
     */
    private void setWidth() {
        WindowManager m = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = (int) (display.getWidth() * 0.875);
        lp.alpha = 1;
        window.setAttributes(lp);
    }

    /**
     * 设置点击对话框外是否消框
     *
     * @param cancel
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleView.setText(title);
    }


    public  interface OnItemClick{
        public void click(int i);
    }



    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }

}
