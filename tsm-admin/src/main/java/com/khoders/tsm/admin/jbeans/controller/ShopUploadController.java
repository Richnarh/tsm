package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.BeansUtil;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.DefaultService;
import com.khoders.tsm.admin.listener.AppSession;
import com.khoders.tsm.admin.services.StockService;
import com.khoders.tsm.admin.services.XtractService;
import com.khoders.tsm.dto.StockDetails;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.enums.LocType;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Richard Narh
 */
@Named(value = "shopUploadController")
@SessionScoped
public class ShopUploadController implements Serializable {

    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private StockService stockService;
    @Inject private DefaultService ds;
    @Inject private XtractService xtractService;

    private StockDetails stockDetails = new StockDetails();
    private List<StockDetails> stockDetailList = new LinkedList<>();

    private UploadedFile file = null;
    private Location selectedShop;
    private CompanyBranch selectedBranch;

    public void getShop() {
        selectedShop = stockService.findLocationByBranch(selectedBranch, LocType.SHOP);
    }

    public String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    public void uploadStock() {
        if (selectedBranch == null) {
            Msg.error("Please select a branch.");
            return;
        }
        if (file.getSize() < 1) {
            Msg.error("No excel file is selected!");
            return;
        }
        try {
            String extension = getFileExtension(file.getFileName());

            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            sheet.removeRow(sheet.getRow(0));
            Iterator<Row> iterator = sheet.iterator();
            System.out.println("Starting....");
            int c = 0;
            while (iterator.hasNext()) {
                c++;
                stockDetails = new StockDetails();
                Row currentRow = iterator.next();
                String productName = BeansUtil.objToString(currentRow.getCell(0));
                if (productName != null && !productName.isEmpty()) {
                    stockDetails.setProductName(productName.trim());
                }
                String productType = BeansUtil.objToString(currentRow.getCell(1));
                if (productType != null && !productType.isEmpty()) {
                    stockDetails.setProductType(productType.trim());
                }
                String reorderLevel = BeansUtil.objToString(currentRow.getCell(2));
                if (reorderLevel != null && !reorderLevel.isEmpty()) {
                    stockDetails.setReorderLevel(BeansUtil.objToInteger(reorderLevel));
                }
                String qtyInWarehouse = BeansUtil.objToString(currentRow.getCell(3));
                if (qtyInWarehouse != null && !qtyInWarehouse.isEmpty()) {
                    stockDetails.setQtyInWarehouse(BeansUtil.objToDouble(qtyInWarehouse));
                }
                String qtyInShop = BeansUtil.objToString(currentRow.getCell(4));
                if (qtyInShop != null && !qtyInShop.isEmpty()) {
                    stockDetails.setQtyInShop(BeansUtil.objToDouble(qtyInShop));
                }
                String costPrice = BeansUtil.objToString(currentRow.getCell(5));
                if (costPrice != null && !costPrice.isEmpty()) {
                    stockDetails.setCostPrice(BeansUtil.objToDouble(costPrice));
                }
                String wprice = BeansUtil.objToString(currentRow.getCell(6));
                if (wprice != null && !wprice.isEmpty()) {
                    stockDetails.setWprice(BeansUtil.objToDouble(wprice));
                }

                String retailPrice = BeansUtil.objToString(currentRow.getCell(7));
                if (retailPrice != null && !retailPrice.isEmpty()) {
                    stockDetails.setRetailPrice(BeansUtil.objToDouble(retailPrice));
                }

                String packaging = BeansUtil.objToString(currentRow.getCell(8));
                if (packaging != null && !packaging.isEmpty()) {
                    stockDetails.setPackaging(BeansUtil.objToString(packaging));
                }
                String units = BeansUtil.objToString(currentRow.getCell(9));
                if (units != null && !units.isEmpty()) {
                    stockDetails.setUnitsMeasurement(units);
                }
                String unitsInPkg = BeansUtil.objToString(currentRow.getCell(10));
                if (unitsInPkg != null && !unitsInPkg.isEmpty()) {
                    stockDetails.setUnitsInPackage(BeansUtil.objToDouble(unitsInPkg));
                }

                stockDetailList.add(stockDetails);
//                appSession.logEvent("Stock Upload", null, "Inventory Uploads");
                System.out.println("Iteration " + c + " done!");
            }
        } catch (IOException e) {
        }
    }

    public void saveUpload() {
        if (selectedBranch == null) {
            Msg.error("Please select a branch.");
            return;
        }
        try {
            int c = 0;
            if (stockDetailList.isEmpty()) {
                return;
            }
            
            if (selectedShop == null) {
                Msg.error("Please create a shop for branch: " + selectedBranch.getBranchName());
                return;
            }

            boolean uploads = xtractService.saveUpload(stockDetailList, selectedBranch);
            if(uploads){ 
                for (StockDetails stockData : stockDetailList) {
                    Inventory inventory = ds.getProduct(ds.getProduct(stockData.getProductName()), ds.getUnits(stockData.getUnitsMeasurement()));
                    if (inventory == null) {
                        inventory = new Inventory();
                        inventory.setProduct(ds.getProduct(stockData.getProductName()));
                        inventory.setUnitMeasurement(ds.getUnits(stockData.getUnitsMeasurement()));
                        inventory.setPackagePrice(stockData.getRetailPrice());
                        inventory.setUnitsInPackage(stockData.getUnitsInPackage());
                        inventory.setWprice(stockData.getWprice());
                        inventory.setUserAccount(appSession.getCurrentUser());
                        inventory.setCompanyBranch(selectedBranch);
                        inventory.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        inventory.setLocation(selectedShop);
                        inventory.setQtyInShop(stockData.getQtyInShop());
                        inventory.setDescription("Inventory Upload on: " + LocalDate.now());
                        inventory.setDataSource("Inventory Upload dated: " + LocalDate.now());
                        crudApi.save(inventory);
                    }
                }
                Msg.info("Upload saved successfully!");
            }
        } catch (Exception e) {
        }
    }

    public void clear() {
        stockDetailList = new LinkedList<>();
        file = null;
        stockDetails = new StockDetails();
        selectedShop = null;
        selectedBranch = null;
        file = null;
        SystemUtils.resetJsfUI();
    }

    public Location getSelectedShop() {
        return selectedShop;
    }

    public void setSelectedShop(Location selectedShop) {
        this.selectedShop = selectedShop;
    }

    public CompanyBranch getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(CompanyBranch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public List<StockDetails> getStockDetailList() {
        return stockDetailList;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
