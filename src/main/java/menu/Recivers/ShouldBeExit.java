package menu.Recivers;


import menu.CarSystem;

public interface ShouldBeExit {
     default void exit(){
         System.out.println("Bye!");
         CarSystem.isWork = false; //что-то с этим сделать
     }
}
