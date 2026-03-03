# ADV Shop (eshop)

Aplikasi e-commerce sederhana menggunakan Spring Boot.  

Link deployment : https://past-atalanta-b-felesia-junelus-2406354152-c09ae512.koyeb.app/ 

---

## Modul 1 - Coding Standards

### Refleksi 1

Selama mengerjakan fitur create product dan delete product, saya mencoba menerapkan beberapa prinsip clean code yang sudah dipelajari di modul ini:

1. **Meaningful Names** — Saya berusaha memberi nama variabel dan method yang jelas, misalnya `createProductPage` untuk menampilkan halaman create dan `deleteProduct` untuk menghapus produk. Dengan begitu, orang lain yang membaca kode bisa langsung paham fungsinya tanpa harus baca komentar.

2. **Single Responsibility** — Saya memisahkan controller, service, dan repository ke class masing-masing supaya setiap class hanya punya satu tanggung jawab. Misalnya `HomeController` saya pisahkan dari `ProductController` karena walaupun sama-sama controller, tugasnya berbeda.

3. **Penggunaan Interface** — Saya membuat interface `ProductService` yang diimplementasikan oleh `ProductServiceImpl`. Awalnya saya bingung kenapa perlu buat interface kalau implementasinya cuma satu, tapi setelah dipelajari ternyata ini berguna untuk mempermudah testing karena bisa di-mock.

4. **Lombok** — Saya pakai `@Getter` dan `@Setter` di model `Product` supaya tidak perlu menulis getter setter manual yang membuat kode jadi panjang.

Selain itu, untuk secure coding saya sudah menggunakan method `POST` untuk operasi create dan delete (bukan `GET`), dan menggunakan `UUID` untuk generate ID produk supaya tidak mudah ditebak.

Namun, setelah saya review kembali, ada beberapa hal yang menurut saya bisa diperbaiki:

- Saya belum menambahkan **validasi input**, jadi user bisa saja mengirim nama produk kosong atau quantity negatif dan tetap berhasil tersimpan.
- Method `findById` mengembalikan `null` kalau produk tidak ditemukan. Ini bisa menyebabkan `NullPointerException` di controller. Sebaiknya bisa menggunakan `Optional` atau throw exception.
- Ada **inkonsistensi pada redirect** — di `createProductPost` saya pakai `redirect:list` (relatif) tapi di `deleteProduct` saya pakai `redirect:/product/list` (absolut). Seharusnya konsisten.
- Parameter `Model model` di method `createProductPost` tidak digunakan, seharusnya dihapus saja supaya lebih bersih.

### Refleksi 2

1. Setelah menulis unit test, jujur saya merasa lebih percaya diri dengan kode yang sudah saya buat karena kalau ada yang error, test-nya langsung ketahuan. Menurut saya, tidak ada aturan pasti berapa jumlah unit test yang harus dibuat dalam satu class. Yang penting, setiap method yang punya logic penting sudah ada test-nya, baik untuk kasus normal maupun kasus-kasus aneh (edge case) seperti input null atau data yang tidak ditemukan. Saya juga belajar tentang code coverage, yaitu metrik yang mengukur seberapa banyak baris kode kita yang dijalankan oleh test. Kalau code coverage 100%, apakah berarti kode kita pasti bebas bug? Menurut saya **tidak**. Coverage 100% cuma berarti semua baris kode sudah pernah dieksekusi, tapi belum tentu semua kemungkinan input atau skenario sudah diuji. Misalnya, kode saya mungkin sudah 100% coverage, tapi belum tentu saya sudah menguji kasus quantity negatif atau nama produk yang sangat panjang.

2. Kalau saya diminta membuat functional test suite baru yang mirip dengan `CreateProductFunctionalTest` (misalnya untuk memverifikasi jumlah item di product list), dan saya langsung copy-paste setup yang sama (seperti `@LocalServerPort`, `baseUrl`, `@BeforeEach`, dll) ke class baru, menurut saya itu akan mengurangi kualitas kode karena terjadi **duplikasi kode (code duplication)**. Ini melanggar prinsip **DRY (Don't Repeat Yourself)** — kalau nanti ada perubahan pada setup (misalnya URL base-nya berubah), saya harus mengubahnya di banyak tempat dan rawan lupa.

   Untuk memperbaikinya, menurut saya bisa membuat **base class** seperti `BaseFunctionalTest` yang berisi setup procedures dan instance variables yang sama (`serverPort`, `testBaseUrl`, `baseUrl`, `@BeforeEach`). Kemudian, setiap functional test class tinggal meng-extend base class tersebut. Dengan begitu, kode setup hanya ditulis sekali dan setiap test class langsung bisa pakai tanpa duplikasi.

---

## Module 2 - Continuous Integration & Continuous Deployment (CI/CD)

### Reflection

**1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.**
Selama ngerjain *exercise* ini, aku benerin beberapa masalah kualitas kode dan keamanan yang dikasih tahu sama *tools*-nya:
* **Masalah Token-Permissions (OSSF Scorecard):** Scorecard ngasih peringatan kalau file GitHub Actions aku (`ci.yml` dan `build.yml`) belum diatur izinnya (*permissions*). Ini lumayan bahaya karena tokennya bisa punya akses terlalu bebas. Cara aku benerinnya cukup dengan nambahin baris `permissions: read-all` di bagian atas file, jadi tokennya cuma dikasih izin buat baca doang (*read-only*) biar lebih aman.
* **Tes yang kosong / Missing assertions (SonarCloud):** SonarCloud ngedeteksi ada *code smell* di file `EshopApplicationTests.java`. Ternyata method `contextLoads()` dan `testMainMethod()` punyaku isinya masih kosong dan nggak ngecek apa-apa (*nggak ada assertion*). Aku benerin ini dengan cara nambahin `assertTrue(true)` buat ngasih tahu kalau aplikasinya beneran berhasil dimuat, dan nambahin `assertDoesNotThrow()` buat mastiin method `main`-nya bisa jalan tanpa ada *error* atau *crash*.

**2. Look at your CI/CD workflows (GitHub Actions). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!**
Iya, menurut aku implementasi yang aku buat sekarang udah memenuhi definisi dari *Continuous Integration* (CI) dan *Continuous Deployment* (CD). 
Alasannya, buat bagian CI, tiap kali aku nge-*push* kode atau bikin *Pull Request*, GitHub Actions bakal otomatis ngejalanin *unit test* (`ci.yml`), ngecek keamanan (*supply-chain*) pakai Scorecard, dan nyari *bug* atau *code smell* pakai SonarCloud (`build.yml`). Jadi kodenya dipastiin aman dan jalan dulu sebelum digabungin. 
Terus buat bagian CD-nya, aku udah ngebungkus aplikasinya pakai `Dockerfile` dan nyambungin repo ini ke platform Koyeb. Jadi, tiap kali kode yang udah aman tadi di-*merge* ke branch `main`, Koyeb bakal otomatis narik kodenya dan langsung nge-*deploy* aplikasinya ke internet biar bisa dipakai sama *user*.

---

## Module 3 - Maintainability & OO Principles

### Reflection

**1) Prinsip SOLID yang diterapkan pada proyek:**

- **SRP (Single Responsibility Principle):** Sebelumnya, `CarController` didefinisikan di dalam file `ProductController.java` dan bahkan `extends ProductController`. Ini melanggar SRP karena satu file punya dua tanggung jawab. Aku perbaiki dengan **memisahkan `CarController` ke file sendiri** dan **menghapus inheritance** yang tidak perlu, sehingga setiap controller hanya bertanggung jawab atas satu entity.

- **OCP (Open/Closed Principle):** Aku membuat Repository interface dan mengimplementasikannya pada `ProductRepository` dan `CarRepository`. Hal ini mengizinkan kita menambahkan Repository baru yang mengimplementasikan interface tersebut tanpa mengubah kode yang telah ada.

- **LSP (Liskov Substitution Principle):** Setelah perbaikan SRP, semua implementasi service (`ProductServiceImpl`, `CarServiceImpl`) bisa menggantikan interface-nya tanpa merusak program. Sebelumnya, `CarController extends ProductController` melanggar LSP karena CarController tidak bisa sepenuhnya menggantikan ProductController.

- **ISP (Interface Segregation Principle):** Interface `ProductService` dan `CarService` sudah kecil dan fokus, masing-masing hanya berisi 5 method CRUD yang semuanya dibutuhkan oleh client-nya. Tidak ada "fat interface" yang memaksa client mengimplementasikan method yang tidak relevan.

- **DIP (Dependency Inversion Principle):** Aku menambahkan **interface untuk repository** (`ProductRepositoryInterface`, `CarRepositoryInterface`). Sebelumnya, service layer bergantung langsung pada class konkret repository — sekarang service bergantung pada abstraksi (interface), sesuai prinsip DIP. Lalu aku mengganti tipe dari variabel carService pada CarController menjadi CarService (sebelumnya CarServiceImpl). Hal ini dilakukan karena CarController seharusnya bergantung pada abstraksi (interface) dibandingkan dengan implementasi.

**2) Keuntungan menerapkan prinsip SOLID:**

- Dengan **SRP**, kalau ada perubahan pada fitur Car, aku cukup ubah `CarController.java` saja tanpa khawatir merusak fitur Product. Misalnya, menambah endpoint baru untuk Car tidak akan berpengaruh sama sekali ke `ProductController`.
- Dengan **OCP**, kalau suatu hari mau ganti penyimpanan dari in-memory `ArrayList` ke database (JPA), aku cukup buat class baru `ProductRepositoryJpa implements ProductRepositoryInterface` tanpa perlu mengubah kode repository yang sudah ada.
- Dengan **DIP**, `CarController` bergantung pada interface `CarService` (bukan `CarServiceImpl`), sehingga kalau mau ganti implementasi service, controller tidak perlu diubah. Begitu juga service yang bergantung pada repository interface, bukan class konkret.

**3) Kerugian kalau tidak menerapkan prinsip SOLID:**

- Tanpa **SRP**, seperti yang terjadi sebelumnya di mana `CarController` dan `ProductController` digabung dalam satu file, perubahan kecil pada satu fitur bisa tidak sengaja merusak fitur lain. Misalnya, mengubah mapping URL Product bisa ikut mempengaruhi Car karena `CarController extends ProductController`.
- Tanpa **OCP**, kalau mau mengganti cara penyimpanan data (misal dari `ArrayList` ke database), kita harus mengubah kode `ProductRepository` yang sudah ada, bukan cukup membuat implementasi baru. Ini berisiko merusak fitur yang sebelumnya sudah berjalan.
- Tanpa **DIP**, `CarController` yang bergantung langsung pada `CarServiceImpl` (bukan interface `CarService`) akan sulit di-test dan sulit diganti implementasinya. Hal yang sama berlaku untuk service yang bergantung pada class konkret repository — kita tidak bisa dengan mudah mock atau swap implementasinya.