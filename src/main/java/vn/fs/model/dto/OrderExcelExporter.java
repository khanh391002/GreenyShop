package vn.fs.model.dto;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;
import vn.fs.model.entities.Order;

@Data
public class OrderExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private List<Order> listOrDetails;

	public OrderExcelExporter(List<Order> listOrDetails) {

		this.listOrDetails = listOrDetails;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("OrderDetails");
	}

	private void writeHeaderRow() {

		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);
		cell.setCellValue("Mã đơn hàng");

		cell = row.createCell(1);
		cell.setCellValue("Người đặt hàng");

		cell = row.createCell(2);
		cell.setCellValue("Ngày đặt hàng");

		cell = row.createCell(3);
		cell.setCellValue("Tổng tiền");

		cell = row.createCell(4);
		cell.setCellValue("Số điện thoại");

		cell = row.createCell(5);
		cell.setCellValue("Địa chỉ");

		cell = row.createCell(6);
		cell.setCellValue("Trạng thái đơn hàng");

	}

	private void writeDataRows() {
		int rowCount = 1;
		for (Order order : listOrDetails) {
			Row row = sheet.createRow(rowCount++);

			Cell cell = row.createCell(0);
			cell.setCellValue(order.getOrderId());

			cell = row.createCell(1);
			cell.setCellValue(order.getUser().getName());

			cell = row.createCell(2);
			cell.setCellValue(order.getOrderDate().toString());

			cell = row.createCell(3);
			cell.setCellValue(order.getAmount());

			cell = row.createCell(4);
			cell.setCellValue(order.getPhone());

			cell = row.createCell(5);
			cell.setCellValue(order.getAddress());

			cell = row.createCell(6);
			switch (order.getStatus()) {
			case 0:
				cell.setCellValue("Chưa xác nhận");
				break;
			case 1:
				cell.setCellValue("Đang giao hàng");
				break;
			case 2:
				cell.setCellValue("Đã thanh toán");
				break;
			case 3:
				cell.setCellValue("Đã Huỷ");
				break;
			default:
				break;
			}
		}

	}

	public void export(HttpServletResponse response) throws IOException {

		writeHeaderRow();
		writeDataRows();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

}
