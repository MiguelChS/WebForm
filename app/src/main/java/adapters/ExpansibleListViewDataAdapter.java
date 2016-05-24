package adapters;

/**
 * Created by jn185090 on 5/17/2016.
 */
public class ExpansibleListViewDataAdapter {
    private String group;
    private String[] childGroup;

    public ExpansibleListViewDataAdapter(String group, String[] childGroup) {
        this.group = group;
        this.childGroup = childGroup;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String[] getChildGroup() {
        return childGroup;
    }

    public void setChildGroup(String[] childGroup) {
        this.childGroup = childGroup;
    }
}
