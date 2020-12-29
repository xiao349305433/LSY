/**
 * author :  lipan
 * filename :  ViewHolder.java
 * create_time : 2014-3-11 上午11:41:14
 */
package met.hx.com.librarybase.some_utils;

import android.util.SparseArray;
import android.view.View;

/**
 * @create_time : 2014-3-11 上午11:41:14
 * @desc : ViewHolder工具类，用于缓存view对象，节省每次都从布局文件中获取view的开销
 * <p>
 * 使用方法如下：
 * public View getView(int position, View convertView, ViewGroup parent)
 * {
 * if(null == convertView)
 * {
 * View view = View.inflate(mContext, R.layout.file_item, null);
 * convertView = view.findViewById(R.id.relaGrid);
 * }
 * <p>
 * ImageView image = ViewHolder.get(convertView, R.id.fileImg);
 * TextView text = ViewHolder.get(convertView, R.id.fileName);
 * //....
 * }
 * @update_time :
 * @update_desc :
 */
public class ViewHolder {
    // I added a generic return type to reduce the casting noise in client code
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
