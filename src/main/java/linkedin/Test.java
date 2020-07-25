package linkedin;

public class Test {


    public static void main(String[] args) {

        System.out.println("abce" instanceof  java.lang.String);

        System.out.println("abcd".getClass().getSimpleName());

        System.out.println(a(1));


        try{

            System.out.println("A");
            somethingBad();
            System.out.println("B");
        }catch (Exception e){
            System.out.println("C");
        }catch (Error error){
            System.out.println("Error");
        }
        finally{
            System.out.println("D");
        }

    }


    public static Exception a(int i){

        if (i > 0){
            return new Exception();
        }else{
            throw new RuntimeException();
        }

    }


    public static void  somethingBad(){

        throw new Error();
        //throw new RuntimeException();

    }



}
