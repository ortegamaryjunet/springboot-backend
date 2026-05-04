package com.mobileApplication.services.web;

import org.springframework.stereotype.Service;

import com.mobileApplication.repositories.web.RecepEquipment;
import com.mobileApplication.repositories.web.RecepMedicine;
import com.mobileApplication.repositories.web.RecepSupply;

@Service
public class RecepInventoryService {

	private final RecepMedicine medicineRepository;
    private final RecepSupply supplyRepository;
    private final RecepEquipment equipmentRepository;

    public RecepInventoryService(RecepMedicine medicineRepository,
    							RecepSupply supplyRepository,
    							RecepEquipment equipmentRepository) {
        this.medicineRepository = medicineRepository;
        this.supplyRepository = supplyRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public long getLowStockMedicineCount() {
        return medicineRepository.findAll()
                .stream()
                .filter(item -> item.getQuantity() <= item.getLowStockThreshold())
                .count();
    }

    public long getLowStockSupplyCount() {
        return supplyRepository.findAll()
                .stream()
                .filter(item -> item.getQuantity() <= item.getLowStockThreshold())
                .count();
    }

    public long getLowStockEquipmentCount() {
        return equipmentRepository.findAll()
                .stream()
                .filter(item -> item.getQuantity() <= item.getLowStockThreshold())
                .count();
    }
    
}
