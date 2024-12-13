module simulador.fxglsimulador {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens simulador.fxglsimulador to javafx.fxml;
    exports simulador.fxglsimulador;
}