public class FibonacciLab {
    static class FibonacciNumber {
        private int index;
        private long value;
        public FibonacciNumber(int index, long value) {
            this.index = index;
            this.value = value;
        }
        
        public static FibonacciNumber calculateNegativeFibonacci(int n) {
            if (n == 0) {
                return new FibonacciNumber(0, 0);
            }
            
            int positiveN = Math.abs(n);
            long positiveFib = calculatePositiveFibonacci(positiveN);
            long negativeFib = (positiveN % 2 == 1) ? positiveFib : -positiveFib;
            
            return new FibonacciNumber(n, negativeFib);
        }
        private static long calculatePositiveFibonacci(int n) {
            if (n == 1 || n == 2) return 1;
            
            long a = 1, b = 1;
            for (int i = 3; i <= n; i++) {
                long temp = a + b;
                a = b;
                b = temp;
            }
            return b;
        }
        

        public int getIndex() { return index; }
        public long getValue() { return value; }
        
        @Override
        public String toString() {
            return "F(" + index + ") = " + value;
        }
    }
    
    public static void main(String[] args) {
        
        int[] testValues = {0, -1, -2, -3, -4, -5, -6, -7, -8};
        
        for (int n : testValues) {
            FibonacciNumber result = FibonacciNumber.calculateNegativeFibonacci(n);
            System.out.println(result);
        }
        
    }
}
