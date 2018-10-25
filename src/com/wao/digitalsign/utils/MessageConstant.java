/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsign.utils;

/**
 *
 * @author BaoBang
 */
public interface MessageConstant {

    public static String ALL_BILL_SIGNED = "Tất cả hóa đơn đã được kí";
    public static String KEYSTORE_EXPIRATION = "Chữ kí số đã hết hạn";
    public static String MESSAGE_FINISHED_SIGN = "Hoàn thành kí hóa đơn";

    
    String MESSAGE_LOADING = "Tải dữ liệu...";

    String MESSAGE_TITLE = "Thông báo";
    String WARNING_TITLE = "Cảnh báo";
    String ERROR_TITLE = "Lỗi";

    // loading message
    String DOWNLOAD_FILE_MESSAGE = "Tải danh sách file";

    // Panel List Order
    String NONE_CHOOSE_ITEM_MESSAGE = "Vui lòng chọn hóa đơn cần kí";

    // Panel KeyStore List
    String NONE_CHOOSE_KEYSTORE_MESSAGE = "Vui lòng chọn chữ kí số";
    String SIGNING_FILE_MESSAGE = "Đang kí file...";
    String SIGNING_SUCCESS_MESSAGE = "Kí thành công";
    String KEYSTORE_NOT_SUPPORT_MESSAGE = "Token không hỗ trợ kí file.";
    String NO_SUCH_ALGORITHM_MESSAGE = "Thuật toán mã hóa không đúng";
    String CERTFICATE_MESSAGE = "Chứng chỉ xác thực không đúng";
    String IOEXCEPTION_MESSAGE = "Không thê truy xuất file";
    String UNRECOVERABLE_KEYEXCEPTION_MESSAGE = "Không thể khôi phục khóa";

    // Exception
    String FILE_NOT_EXITS_EXCEPTION = "File không tồn tại";
    String SIGN_FILE_FAILED_EXCEPTION = "Kí thất bại";
    String NOT_FIND_CERTIFICATE_EXCEPTION = "Không tìm thấy chứng chỉ";
    String KEYSTORE_NOT_FOUND_EXCEPTION = "Không tìm thấy khóa";
    String URL_INVALID_EXEPTION = "";
    String NOT_CONNECT_TO_SERVER_EXCEPTION = "Lỗi kết nối đến server";
}
