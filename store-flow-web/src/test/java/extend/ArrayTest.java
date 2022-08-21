package extend;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/20 18:31
 */
@SpringBootTest
public class ArrayTest {


    @Test
    public void arrayTest() {

        int[] nums1 = new int[]{2, 3, 4};
        int[] nums2 = new int[]{1, 2, 3, 4};

        int[] arr = new int[7];

        int length1 = nums1.length - 1;
        int length2 = nums2.length - 1;
        int arrLength = arr.length - 1;

        while (length1 >= 0 || length2 >= 0) {
            int a = length1 >= 0 ? nums1[length1] : 0;
            int b = length2 >= 0 ? nums2[length2] : 0;
            arr[arrLength--] = a >= b ? nums1[length1--] : nums2[length2--];
        }
        System.out.println(Arrays.toString(arr));
    }
}
