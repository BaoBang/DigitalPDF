/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.ui;

import com.wao.digitalsign.utils.FXUtils;
import com.wao.digitalsign.callback.ConfirmCallBack;
import com.wao.digitalsign.callback.ApiCallBack;
import com.wao.digitalsign.api.APIService;
import com.wao.digitalsign.api.ApiUtils;
import com.wao.digitalsign.api.requestbody.GetFileBody;
import com.wao.digitalsign.api.response.Bill;
import com.wao.digitalsign.api.response.Result;
import com.wao.digitalsign.errorexception.CanNotGetKeyStoreException;
import com.wao.digitalsign.utils.MessageConstant;
import com.wao.digitalsign.utils.Utils;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author BaoBang
 */
public class MainController implements Initializable, Observer {

    private APIService mAPIService;
    private List<String> mAliases;
    private ObservableList<RowItem> mRowItems;
    private String id;

    public static String keyAlias = "";

    private int oldFileSize = 0; // Kích thước File.listRoots()

    @FXML
    private ComboBox<String> cbKeyStore;
    @FXML
    TableView<RowItem> tbListBill;
    @FXML
    TableColumn<RowItem, CheckBox> colChekcBox;
    @FXML
    TableColumn<RowItem, String> colOrderId;
    @FXML
    TableColumn<RowItem, String> colOrderName;
    @FXML
    TableColumn<RowItem, String> colStatus;

    @FXML
    Button btnSign;

    @FXML
    Button btnCancel;

    private CheckBox cbHeader;

    public MainController() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Thực hiện khi người dùng nhấn button Kí HĐ 1. Kiểm tra KeyStore alias có
     * được chọn hay không? 1.1. Thông báo nếu chưa chọn 2. Kiểm tra hạn sử dụng
     * của KeyStore 3. Kiểm tra tất cả các row đã kí chưa 3.1. Thông báo nếu tất
     * cả đã được kí 4. Kiểm tra có row item nào được chọn hay không? 4.1. Thông
     * báo nếu không có row item nào được chọn 5. Diable button Kí HĐ và button
     * Thoát 6. Thực hiện kí HĐ
     *
     * @param event ActionEvent
     */
    @FXML
    private void handleClickSign(ActionEvent event) {

        // 1. Kiểm tra KeyStore alias có được chọn hay không?
        int keyStoreSelectedIndex = cbKeyStore.getSelectionModel().getSelectedIndex();
        if (keyStoreSelectedIndex == -1) {
            // 1.1. Thông báo nếu chưa chọn
            FXUtils.showMessage(
                    MessageConstant.WARNING_TITLE,
                    MessageConstant.NONE_CHOOSE_KEYSTORE_MESSAGE,
                    "",
                    Alert.AlertType.WARNING);
            return;
        }
        keyAlias = cbKeyStore.getSelectionModel().getSelectedItem();
        // 2. Kiểm tra hạn sử dụng của KeyStore
        if (Utils.checkExpirationDate(keyAlias)) {
            FXUtils.showMessage(
                    MessageConstant.WARNING_TITLE,
                    MessageConstant.KEYSTORE_EXPIRATION,
                    "",
                    Alert.AlertType.WARNING);
            return;
        }

        // 3. Kiểm tra tất cả các row đã kí chưa
        if (isSucessedTask()) {
            FXUtils.showMessage(
                    MessageConstant.MESSAGE_TITLE,
                    MessageConstant.ALL_BILL_SIGNED,
                    "",
                    Alert.AlertType.INFORMATION);
            return;
        }
        // 4. Kiểm tra có row item nào được chọn hay không?
        int rowCountSeclectdIndex = getNumberRowChecked();
        if (rowCountSeclectdIndex <= 0) {
            // 4.1. Thông báo nếu không có row item nào được chọn
            FXUtils.showMessage(
                    MessageConstant.WARNING_TITLE,
                    MessageConstant.NONE_CHOOSE_ITEM_MESSAGE,
                    "",
                    Alert.AlertType.WARNING);
            return;
        } else {
            // 5. Diable button Kí HĐ và button Thoát
            disableButton(true);
        }
        System.out.println(keyAlias);
        // 6. Thực hiện kí HĐ
        mRowItems.forEach((item) -> {
            if (item.getIsChecked() && item.getState() != RowItem.COMPLETED) {
                item.start();
            }
        });
    }

    /**
     * Thực hiện khi người dùng nhấn button Thoát
     *
     * @param event ActionEvent
     */
    @FXML
    private void handleClickCancel(ActionEvent event) {
        stop();
    }

    /**
     * Khởi tạo
     *
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mAPIService = ApiUtils.getAPIService();
        initTableView();
        waitForNotifying();

    }

    /**
     * Nạp dữ liệu vào TableView và ComboBox
     *
     * @param callBack
     * @throws com.wao.digitalsign.errorexception.CanNotGetKeyStoreException
     */
    public void loadData(ApiCallBack callBack) throws CanNotGetKeyStoreException {
        loadKeyStoreIntoComboBox();
        callApiGetData(callBack);
    }

    /**
     * Lấy danh sách KeyStore alias Nạp danh sách KeyStore alias vào ComboBox
     *
     * @throws CanNotGetKeyStoreException
     */
    private void loadKeyStoreIntoComboBox() throws CanNotGetKeyStoreException {
        mAliases = Utils.getKeystores();
        Platform.runLater(() -> {
            cbKeyStore.setItems(FXCollections.observableList(mAliases));
            cbKeyStore.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Choose KeyStore Aliase: {0}", newValue);

            });
        });

    }

    /**
     * Nạp danh sách RowItem(danh sách hóa đơn) vào TableView
     */
    private void loadBillIntoTableView(String id, List<String> data) {
        mRowItems = getBill(id, data);
        tbListBill.setItems(mRowItems);
    }

    /**
     *
     *
     * @param id String : Danh sách mã hóa đơn
     * @param data: Danh sách link của hóa đơn tương ứng với mã hóa đơn
     */
    private ObservableList<RowItem> getBill(String id, List<String> data) {
        List<RowItem> list = new ArrayList();
        String items[] = id.split(",");
        if (items.length == data.size()) {
            for (int i = 0; i < items.length; i++) {
                RowItem item = new RowItem(new Bill(items[i], data.get(i)));
                item.addObserver(this);
                list.add(item);
            }
        }
        ObservableList observableList = FXCollections.observableList(list);

        // NOTE: When the column check boxes (with checked and unchecked states)
        // are checked / unchecked the header check box updates accordingly.
        // When all rows are checked - the header is checked
        // When all rows are unchecked - the header is unchecked
        // When some rows are checked / unchecked - the header is indeterminate
        observableList.addListener((ListChangeListener) change -> {

            while (change.next()) {
                if (change.wasUpdated()) {
                    int checked = getNumberRowChecked();

                    cbHeader.setSelected(tbListBill.getItems().size() == checked);
                }
            }
        });

        return observableList;

    }

    /**
     * Khởi tạo TableView I. Header ChexBox 1. Cột chọn hóa đơn 1.1. Thêm sự
     * kiện khi checbox được chọn 2. Cột mã hóa đơn 3. Cột link hóa đơn 4. Cột
     * trạng thái hóa đơn
     */
    private void initTableView() {
        // 1. Header ChexBox
        cbHeader = new CheckBox();
        cbHeader.setUserData(colChekcBox);
        cbHeader.setOnAction(handleSelectAllCheckbox());
        this.colChekcBox.setGraphic(cbHeader);

        // 2. Cột chọn hóa đơn
        colChekcBox.setCellValueFactory((TableColumn.CellDataFeatures<RowItem, CheckBox> param) -> {
            RowItem rowItem = param.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(rowItem.getIsChecked());
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                    rowItem.setIsChecked(new_val);
                    checkSelectedAll();
                }
            });

            return new SimpleObjectProperty<>(checkBox);
        });
        // 2. Column ChexBox
        colOrderId.setCellValueFactory(new PropertyValueFactory("id"));
        // 4. Cột link hóa đơn
        colOrderName.setCellValueFactory(new PropertyValueFactory("link"));
        // 5. Cột trạng thái hóa đơn
        colStatus.setCellValueFactory(new PropertyValueFactory("status"));
        // fill colum in table width
        tbListBill.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Sự kiện khi người dụng click vào CheckBox column'header
     *
     * @return EventHandler <ActionEvent>
     */
    private EventHandler<ActionEvent> handleSelectAllCheckbox() {

        return (ActionEvent event) -> {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Click Select all checkbox");
            ObservableList<RowItem> data = tbListBill.getItems();
            for (RowItem rowItem : data) {
                rowItem.setIsChecked(cbHeader.isSelected());
            }
            tbListBill.refresh();
        };
    }

    /**
     * <p>
     * Nếu tất cả các row trong TableView được chọn</p>
     * <p>
     * Thì checked CheckBox column's header</p>
     * <p>
     * Ngược lại uncheck CheckBox column's header</p>
     */
    private void checkSelectedAll() {

        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Kiểm tra tất cả row được check hay không?");
        for (RowItem rowItem : mRowItems) {
            if (!rowItem.getIsChecked()) {
                cbHeader.setSelected(false);
                return;
            }
        }
        cbHeader.setSelected(true);
    }

    /**
     * <p>
     * Gọi api http://api.eprocon.us/DigSin/GetFiles </p>
     * <p>
     * Method GET </p>
     * <p>
     * Body GetFileBody</p>
     *
     * @param callBack ApiCallBack
     */
    private void callApiGetData(ApiCallBack callBack) {

        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Gọi api lấy danh sách hóa đơn");
        mAPIService.getBills(new GetFileBody(id)).enqueue(new Callback<Result<List<String>>>() {
            @Override
            public void onResponse(Call<Result<List<String>>> call, Response<Result<List<String>>> response) {

                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Api lấy danh sách hóa đơn trả về thành công");
                if (response.isSuccessful()) {
                    Result<List<String>> result = response.body();

                    if (result.getResult().getErrorCode() == 0) {
                        loadBillIntoTableView(id, result.getResult().getData());
                        callBack.onSucess();
                    } else {
                        callBack.onFailed(result.getResult().getErrorDescription());

                    }
                } else {
                    callBack.onFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<Result<List<String>>> call, Throwable t) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Api lấy danh sách hóa đơn thất bại");
                callBack.onFailed(MessageConstant.NOT_CONNECT_TO_SERVER_EXCEPTION);
            }

        });
    }

    /**
     * Bật tắt button Kí HĐ và button Thoát
     *
     * @param isDisable
     */
    public void disableButton(boolean isDisable) {
        btnSign.setDisable(isDisable);
        btnCancel.setDisable(isDisable);
    }

    /**
     * <p>
     * Thread kiểm tra có usb được cắm hay rút ra</p>
     * <p>
     * Cập nhật lại danh sách alias nếu có sự kiện trên.</p>
     */
    private void waitForNotifying() {
        Thread t = new Thread(() -> {
            while (true) {
                if (File.listRoots().length - oldFileSize != 0) {
                    oldFileSize = File.listRoots().length;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                    }
                    try {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "USB thay đổi");
                        loadKeyStoreIntoComboBox();
                    } catch (CanNotGetKeyStoreException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
    }

    /**
     * Lắng nghe các notify từ các Observable(RowItem) phát ra Cập nhật
     * TableView
     */
    @Override
    public void update(Observable o, Object arg) {

        if (isEnableTask()) {
            disableButton(false);
             FXUtils.showMessage(
                    MessageConstant.WARNING_TITLE,
                    MessageConstant.MESSAGE_FINISHED_SIGN,
                    "",
                    Alert.AlertType.INFORMATION);
        }

        tbListBill.refresh();
    }

    /**
     * Đếm số lượng hàng trong table được chọn
     *
     * @return checked Số lượng hàng được chọn
     */
    private int getNumberRowChecked() {
        int checked = 0;
        return tbListBill.getItems()
                .stream()
                .filter((rowItem) -> (rowItem.getIsChecked()))
                .map((item) -> 1)
                .reduce(checked, Integer::sum);
    }

    /**
     * Đếm số hàng được chọn đã thực hiện xong rowItem.getIsChecked() == true &&
     * mState == RowItem.COMPLETED || mState == RowItem.ERROR
     */
    private int getRowFinshed() {
        int count = 0;
        count = mRowItems
                .stream()
                .filter((item)
                        -> (item.getIsChecked() && (item.isFinshed())))
                .map((_item) -> 1)
                .reduce(count, Integer::sum);
        return count;
    }

    /**
     * Kiểm tra tất cả row đã hoàn tất kí hđ chưa
     *
     */
    public boolean isEnableTask() {
        if (!mRowItems.stream().noneMatch((rowItem) -> (rowItem.getIsChecked() && !rowItem.isFinshed()))) {
            return false;
        }
        return true;
    }

    /**
     * Kiểm tra tất cả row đã kí thành công chưa
     *
     */
    public boolean isSucessedTask() {
        if (!mRowItems.stream().noneMatch((rowItem) -> (!rowItem.isSucced()))) {
            return false;
        }
        return true;
    }

    /**
     * Thực hiện khi người dùng thoát chương trình 1. Kiểm tra có hóa đơn nào
     * đang thực hiện kí hay không? 2. Hiện thị dialog xác nhận thoát
     */
    public void stop() {
        // 1.Kiểm tra có hóa đơn nào đang thực hiện kí hay không?
        if (!isEnableTask()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/wao/digitalsignpdf/images/logo.png"));
            alert.setTitle(MessageConstant.MESSAGE_TITLE);
            alert.setHeaderText("Đang thực hiện kí hóa đơn, không thể đóng ứng dụng.");

            alert.showAndWait();
            return;
        }

        // 2. Hiện thị dialog xác nhận thoát
        FXUtils.showMessageComfirmation(
                MessageConstant.MESSAGE_TITLE,
                "Bạn muốn thoát ứng dụng?", new ConfirmCallBack() {
            @Override
            public void onOkButton() {
                // Kết thúc chương trình
                Logger logger = Logger.getLogger(MainController.class.getName());
                logger.log(Level.SEVERE, "Đóng ứng dụng");
                System.exit(0);
            }

            @Override
            public void onCancelButton() {
            }
        }
        );

    }
}
