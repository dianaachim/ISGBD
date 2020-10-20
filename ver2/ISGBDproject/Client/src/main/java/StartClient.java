import GUI.MainWindow;
import Service.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartClient extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try
        {

            ApplicationContext context= new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            IService service= (IService) context.getBean("service");

            System.out.println("A intrat un user");
            primaryStage.setTitle("Concurs");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainWindow.fxml"));


            try {
                Parent root = loader.load();
                primaryStage.setScene(new Scene(root));

                MainWindow controller = loader.getController();
                controller.setService(service);
                // root.getStylesheets().add(getClass().getResource("StyleMainWindow.css").toString());
                primaryStage.show();
            } catch (Exception e) {
                System.out.println("Error" + e.getMessage());
            }
        }
        catch (Exception e)
        {
            System.out.println("Error"+e.getMessage());
        }
    }
}
