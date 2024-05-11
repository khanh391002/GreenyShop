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
		cell.setCellValue("Tổng tiền");
		
		cell = row.createCell(2);
		cell.setCellValue("Số điện thoại");
		
		cell = row.createCell(3);
		cell.setCellValue("Địa chỉ");

	}
	
	private void writeDataRows() {
		int rowCount = 1;
		for (Order order : listOrDetails) {
			Row row = sheet.createRow(rowCount++);

			Cell cell = row.createCell(0);
			cell.setCellValue(order.getOrderId());
			
			cell = row.createCell(1);
			cell.setCellValue(order.getAmount());
			
			cell = row.createCell(2);
			cell.setCellValue(order.getPhone());

			cell = row.createCell(3);
			cell.setCellValue(order.getAddress());

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
