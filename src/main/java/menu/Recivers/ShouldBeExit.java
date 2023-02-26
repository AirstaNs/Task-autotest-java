package menu.Recivers;


import menu.FTPSystem;

public interface ShouldBeExit {
     default void exit(){
         System.out.println("Bye!");
         FTPSystem.isWork = false; //что-то с этим сделать
     }
}
