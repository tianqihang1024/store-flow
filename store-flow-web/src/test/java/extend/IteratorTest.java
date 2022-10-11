package extend;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorTest {


    /**
     * 测试 iterator.remove，remove会把当前获取的next移除掉，next和remove的数量需要对应
     */
    @Test
    public void removeTest() {

        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);

        Iterator<Long> iterator = list.iterator();

        while (iterator.hasNext()) {

            Long next = iterator.next();

            iterator.remove();
        }

    }
}
