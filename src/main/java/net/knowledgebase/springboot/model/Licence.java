package net.knowledgebase.springboot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Licence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String client;

    @Column
    private String checksum;

    @Column
    private LocalDateTime dateRequested;

    @Column
    private long employees;

    @Column
    private long companies;

    @Column
    private boolean unlimitedCompanies;

    @Column
    private boolean module1;

    @Column
    private boolean module2;

    @Column
    private boolean module3;

    @Column
    private boolean module4;

    @Column
    private boolean module5;

    @Column
    private boolean module6;

    @Column
    private boolean module7;

    @Column
    private boolean module8;

    @Column
    private boolean module9;

    @Column
    private boolean module10;

    @Column
    private boolean module11;

    @Column
    private boolean module12;

    @Column
    private boolean module13;

    @Column
    private boolean module14;

    @Column
    private boolean module15;

    @Column
    private boolean module16;

    @Column
    private boolean module21;

    @Column
    private boolean module31;

    @Column
    private LocalDateTime seasonalExpiryDate;

    @Column
    private LocalDateTime dateGenerated;

    @Column
    private LocalDateTime expiryDate;

    @Column
    private boolean active;

    @Column
    private boolean readOnly;

    public Licence() {
    }

    public Licence(long id, String client, String checksum, LocalDateTime dateRequested, long employees, long companies, boolean unlimitedCompanies, boolean module1, boolean module2, boolean module3, boolean module4, boolean module5, boolean module6, boolean module7, boolean module8, boolean module9, boolean module10, boolean module11, boolean module12, boolean module13, boolean module14, boolean module15, boolean module16, boolean module21, boolean module31, LocalDateTime seasonalExpiryDate, LocalDateTime dateGenerated, LocalDateTime expiryDate, boolean active, boolean readOnly) {
        this.id = id;
        this.client = client;
        this.checksum = checksum;
        this.dateRequested = dateRequested;
        this.employees = employees;
        this.companies = companies;
        this.unlimitedCompanies = unlimitedCompanies;
        this.module1 = module1;
        this.module2 = module2;
        this.module3 = module3;
        this.module4 = module4;
        this.module5 = module5;
        this.module6 = module6;
        this.module7 = module7;
        this.module8 = module8;
        this.module9 = module9;
        this.module10 = module10;
        this.module11 = module11;
        this.module12 = module12;
        this.module13 = module13;
        this.module14 = module14;
        this.module15 = module15;
        this.module16 = module16;
        this.module21 = module21;
        this.module31 = module31;
        this.seasonalExpiryDate = seasonalExpiryDate;
        this.dateGenerated = dateGenerated;
        this.expiryDate = expiryDate;
        this.active = active;
        this.readOnly = readOnly;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public LocalDateTime getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(LocalDateTime dateRequested) {
        this.dateRequested = dateRequested;
    }

    public long getEmployees() {
        return employees;
    }

    public void setEmployees(long employees) {
        this.employees = employees;
    }

    public long getCompanies() {
        return companies;
    }

    public void setCompanies(long companies) {
        this.companies = companies;
    }

    public boolean isUnlimitedCompanies() {
        return unlimitedCompanies;
    }

    public void setUnlimitedCompanies(boolean unlimitedCompanies) {
        this.unlimitedCompanies = unlimitedCompanies;
    }

    public boolean isModule1() {
        return module1;
    }

    public void setModule1(boolean module1) {
        this.module1 = module1;
    }

    public boolean isModule2() {
        return module2;
    }

    public void setModule2(boolean module2) {
        this.module2 = module2;
    }

    public boolean isModule3() {
        return module3;
    }

    public void setModule3(boolean module3) {
        this.module3 = module3;
    }

    public boolean isModule4() {
        return module4;
    }

    public void setModule4(boolean module4) {
        this.module4 = module4;
    }

    public boolean isModule5() {
        return module5;
    }

    public void setModule5(boolean module5) {
        this.module5 = module5;
    }

    public boolean isModule6() {
        return module6;
    }

    public void setModule6(boolean module6) {
        this.module6 = module6;
    }

    public boolean isModule7() {
        return module7;
    }

    public void setModule7(boolean module7) {
        this.module7 = module7;
    }

    public boolean isModule8() {
        return module8;
    }

    public void setModule8(boolean module8) {
        this.module8 = module8;
    }

    public boolean isModule9() {
        return module9;
    }

    public void setModule9(boolean module9) {
        this.module9 = module9;
    }

    public boolean isModule10() {
        return module10;
    }

    public void setModule10(boolean module10) {
        this.module10 = module10;
    }

    public boolean isModule11() {
        return module11;
    }

    public void setModule11(boolean module11) {
        this.module11 = module11;
    }

    public boolean isModule12() {
        return module12;
    }

    public void setModule12(boolean module12) {
        this.module12 = module12;
    }

    public boolean isModule13() {
        return module13;
    }

    public void setModule13(boolean module13) {
        this.module13 = module13;
    }

    public boolean isModule14() {
        return module14;
    }

    public void setModule14(boolean module14) {
        this.module14 = module14;
    }

    public boolean isModule15() {
        return module15;
    }

    public void setModule15(boolean module15) {
        this.module15 = module15;
    }

    public boolean isModule16() {
        return module16;
    }

    public void setModule16(boolean module16) {
        this.module16 = module16;
    }

    public boolean isModule21() {
        return module21;
    }

    public void setModule21(boolean module21) {
        this.module21 = module21;
    }

    public boolean isModule31() {
        return module31;
    }

    public void setModule31(boolean module31) {
        this.module31 = module31;
    }

    public LocalDateTime getSeasonalExpiryDate() {
        return seasonalExpiryDate;
    }

    public void setSeasonalExpiryDate(LocalDateTime seasonalExpiryDate) {
        this.seasonalExpiryDate = seasonalExpiryDate;
    }

    public LocalDateTime getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(LocalDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
