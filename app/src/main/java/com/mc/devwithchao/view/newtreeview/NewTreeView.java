package com.mc.devwithchao.view.newtreeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.mc.devwithchao.R;

import java.util.ArrayList;
import java.util.List;

import static com.mc.devwithchao.view.newtreeview.Tool.dp2px;


/**
 * Created by MC on 2017/4/7.
 * 对github项目owant的树形结构图进行的改变，在此声明并感谢
 */

public class NewTreeView extends ViewGroup {
    //所有节点的集合
    private List<nodeModel> nodes = new ArrayList<>();
    private List<LineModel> lines = new ArrayList<>();//连接线集合
    private Context context;
    private int startY = 100;//起始点Y轴
    private int ScreenWith;
    private int ScreentHeight;
    private int HoriranSpace = 20;//水平间距
    private int VerticalSpace = 80;//垂直间距
    private int ChildViewWith = 200;
    private int ChildViewheight = 80;
    private ViewControlHandler mViewControlHandler;
    private int Scale=0;
    private int totalWidth;


    public NewTreeView(Context context) {
        super(context);
        mViewControlHandler = new ViewControlHandler(this);
        ScreenWith = ScreenUtils.getScreenW();
        ScreentHeight=ScreenUtils.getScreenH();
        this.context = context;
    }

    public NewTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenWith = ScreenUtils.getScreenW();
        ScreentHeight=ScreenUtils.getScreenH();
        mViewControlHandler = new ViewControlHandler(this);
        this.context = context;
    }

    public NewTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ScreenWith = ScreenUtils.getScreenW();
        ScreentHeight=ScreenUtils.getScreenH();
        mViewControlHandler = new ViewControlHandler(this);
        setClipToPadding(false);
        setClipChildren(false);
        this.context = context;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int offsetX=(totalWidth-ScreenWith)/2+40;
        for (int i = 0; i < nodes.size(); i++) {
            View view = getChildAt(i);
            view.layout(nodes.get(i).getChildX()+offsetX, nodes.get(i).getChildY(), nodes.get(i).getChildX() + ChildViewWith+offsetX, nodes.get(i).getChildY() + ChildViewheight);
        }
        setX(0-offsetX);
    }

        @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count=getChildCount();
        int Viewleft=0;
        int Viewright=0;
        int ViewTop=0;
        int Viewbottom=0;
        // 测量child

        for (int i = 0; i < nodes.size(); i++) {
            Viewleft=(Viewleft<=nodes.get(i).getChildX())?Viewleft:nodes.get(i).getChildX();
            Viewright=(Viewright>=nodes.get(i).getChildX()+ChildViewWith)?Viewright:nodes.get(i).getChildX()+ChildViewWith;
            Viewbottom=Viewbottom>=nodes.get(i).getChildY()+ChildViewheight?Viewbottom:nodes.get(i).getChildY()+ChildViewheight;
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
            totalWidth = Viewright-Viewleft;
        int TotalHeight=Viewbottom-ViewTop;
        int ScaleWidth = totalWidth / ScreenWith;
        int ScaleHeight = TotalHeight / ScreentHeight;
        if (ScaleWidth >= ScaleHeight && ScaleWidth > 1) {
            Scale = ScaleWidth;
        } else if (ScaleHeight >= ScaleWidth && ScaleHeight > 1) {
            Scale = ScaleHeight;
        }
        setMeasuredDimension(totalWidth, TotalHeight);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawTreeLine(canvas, lines);
        super.dispatchDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mViewControlHandler.move(event);
    }

    /**
     * 绘制树形的连线
     *
     * @param canvas
     * @param lines
     */
    private void drawTreeLine(Canvas canvas, List<LineModel> lines) {
        for (int i = 0; i < lines.size(); i++) {
            View from = lines.get(i).getStartNode();
            for (int j = 0; j < lines.get(i).getChildNodes().size(); j++) {
                View to = lines.get(i).getChildNodes().get(j);
                drawLineToView(canvas, from, to);
            }
        }
    }

    /**
     * 绘制两个View直接的连线
     *
     * @param canvas
     * @param from
     * @param to
     */
    private void drawLineToView(Canvas canvas, View from, View to) {

        if (to.getVisibility() == GONE) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        float width = 2f;

        paint.setStrokeWidth(dp2px(context, width));
        paint.setColor(context.getResources().getColor(R.color.result_points));

        int top = from.getTop();
        int formY = top + ChildViewheight;
        int formX = from.getLeft() + ChildViewWith / 2;

        int top1 = to.getTop();
        int toY = to.getTop();
        int toX = to.getLeft() + ChildViewWith / 2;

        Path path = new Path();
        path.moveTo(formX, formY);
        path.lineTo(toX, toY);

        canvas.drawPath(path, paint);
    }

    /**
     * 添加View
     */
    public void layoutView() {
        for (int i = 0; i < nodes.size(); i++) {
            addView(nodes.get(i).getNode());
        }
    }

    /**
     * 添加view
     *
     * @param start  父节点
     * @param childs 子节点集合
     */
    public void addNodeView(NodeView start, NodeView... childs) {
        LineModel lineModel = new LineModel();
        int faX = 0;
        int faY = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (start.equals(nodes.get(i).getNode())) {
                faX = nodes.get(i).getChildX()+ChildViewWith/2;
                faY = nodes.get(i).getChildY() + ChildViewheight + VerticalSpace;
            }
        }
        List<NodeView> childNodes = new ArrayList<>();
        for (int i = 0; i < childs.length; i++) {
            nodeModel nodeModel = new nodeModel();
            nodeModel.setNode(childs[i]);
            if ((i) % 2 != 0) {//奇数，2,4,6,8
                nodeModel.setChildX(faX + i/2 * (ChildViewWith+HoriranSpace)+HoriranSpace/2);
                nodeModel.setChildY(faY);
            } else {//偶数
                nodeModel.setChildX(faX - i/2*(ChildViewWith+HoriranSpace)-ChildViewWith-HoriranSpace/2);
                nodeModel.setChildY(faY);
            }
            childNodes.add(childs[i]);
            nodes.add(nodeModel);
        }
        lineModel.setStartNode(start);
        lineModel.setChildNodes(childNodes);
        lines.add(lineModel);
    }

    /**
     * 添加根节点
     *
     * @param root
     */
    public void addrootNode(NodeView root) {
        ScreenWith = ScreenUtils.getScreenW();
        ScreentHeight=ScreenUtils.getScreenH();
        nodeModel nodeModel = new nodeModel();
        nodeModel.setNode(root);
        nodeModel.setChildX(ScreenWith / 2 - ChildViewWith / 2);
        nodeModel.setChildY(startY);
        nodes.add(nodeModel);
    }

    public class nodeModel {
        private NodeView node;
        private int childX;
        private int childY;

        public NodeView getNode() {
            return node;
        }

        public void setNode(NodeView node) {
            this.node = node;
        }

        public int getChildX() {
            return childX;
        }

        public void setChildX(int childX) {
            this.childX = childX;
        }

        public int getChildY() {
            return childY;
        }

        public void setChildY(int childY) {
            this.childY = childY;
        }
    }

    public class LineModel {
        private NodeView startNode;
        private List<NodeView> childNodes;

        public NodeView getStartNode() {
            return startNode;
        }

        public void setStartNode(NodeView startNode) {
            this.startNode = startNode;
        }

        public List<NodeView> getChildNodes() {
            return childNodes;
        }

        public void setChildNodes(List<NodeView> childNodes) {
            this.childNodes = childNodes;
        }
    }
}
