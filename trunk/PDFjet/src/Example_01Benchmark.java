import com.google.caliper.Benchmark;
import com.google.caliper.Param;

public class Example_01Benchmark {

    @Param({ "100" })
    int pages;
    Example_01 example_01 = new Example_01();
    Example_01_Opt example_01_opt = new Example_01_Opt();

    @Benchmark
    long callExample01(int reps) throws Exception {
        long total = 0;
        for (int i = 0; i < reps; i++) {
            total |= example_01.run_call(pages);
        }
        return total;
    }

    @Benchmark
    long inline_01(int reps) throws Exception {
        long total = 0;
        for (int i = 0; i < reps; i++) {
            total |= example_01.run_inline_01(pages);
        }
        return total;
    }

    @Benchmark
    long inline_02(int reps) throws Exception {
        long total = 0;
        for (int i = 0; i < reps; i++) {
            total |= example_01_opt.run_inline_02(pages);
        }
        return total;
    }
}
