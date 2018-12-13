package com.yofish.kitmodule.baseAdapter.recyclerview;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class DBRCViewType {
    private int type;
    private int layoutId;
    private int varId;

    public DBRCViewType(int type, int layoutId, int varId) {
        this.type = type;
        this.layoutId = layoutId;
        this.varId = varId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getVarId() {
        return varId;
    }

    public void setVarId(int varId) {
        this.varId = varId;
    }
}
