## [4.0.0](https://github.com/RustFields/ScaFi-fields/compare/3.0.0...4.0.0) (2023-07-26)


### ⚠ BREAKING CHANGES

* change foldhoodf and fromExpression signature to have a lambda as last curried argument
* add fromExpression construct and refactor foldhoodf
* Modify nbr signature and implementation
* fix Field.fold implementation
* modified write condition inside nbrf
* change conversion construction function
* remove useless function

### Features

* add a field library containing other useful functions ([bf9baed](https://github.com/RustFields/ScaFi-fields/commit/bf9baed73cbf988d1d47a9a31d22d311e6dbadb2))
* add constructs to fold Fields ([d53278d](https://github.com/RustFields/ScaFi-fields/commit/d53278d284debb2c964257bcb072c5ab2423ae59))
* add defaultable instance ([64fd68f](https://github.com/RustFields/ScaFi-fields/commit/64fd68f43290bb174611f2b239da5683ebb7d94f))
* add extra new fold function that works with defaultables ([857cfb8](https://github.com/RustFields/ScaFi-fields/commit/857cfb8f7d23abc02acebb6636afaff75e77490f))
* add extra syntax inside FieldOps ([ff1115a](https://github.com/RustFields/ScaFi-fields/commit/ff1115adab902fb8fe56a6dac58b157a56bc326f))
* add Field Language Interpreter ([68d5311](https://github.com/RustFields/ScaFi-fields/commit/68d5311ed882bc9f6b4962b8658a847c826c29a0))
* add foldhoodf construct ([537f071](https://github.com/RustFields/ScaFi-fields/commit/537f071e3db22cb02a6d740a6d468603ba864d71))
* add fullyConnectedTopologyMap function ([5cf0f2f](https://github.com/RustFields/ScaFi-fields/commit/5cf0f2f85c91d834e95dc058b1eb1ebb8fb6c70d))
* add neighbouring syntax ([85b8770](https://github.com/RustFields/ScaFi-fields/commit/85b8770be25bacced1fa520f9c128e08d70ff418))
* add new functions ([71a7d97](https://github.com/RustFields/ScaFi-fields/commit/71a7d9772bed6c3a9ace344b0ccfabfd6b6cb694))
* add syntax for folding fields ([6a6a655](https://github.com/RustFields/ScaFi-fields/commit/6a6a655889c48a4ae9838a58cacec79b3a6223d5))
* add syntax for get function in Field ([60fc9a4](https://github.com/RustFields/ScaFi-fields/commit/60fc9a4b98783ad9f7da328437a7a7d632760f72))
* add trait helpful for dependency management ([243f7fa](https://github.com/RustFields/ScaFi-fields/commit/243f7faef63fe011823dedaf5767af6efc5321a5))
* add trait with special syntax for fields ([919c90d](https://github.com/RustFields/ScaFi-fields/commit/919c90dc31e4c8e3273aa1c85cecd553416660b8))
* create auxiliary factory functions for fields ([c6df0f8](https://github.com/RustFields/ScaFi-fields/commit/c6df0f895022d7c227c25bde56b2b63b77935595))
* create class TestByEquivalence ([a518f17](https://github.com/RustFields/ScaFi-fields/commit/a518f1739491ae417f53f6ecabc53599867ff620))
* enable field syntax inside field language ([a368d46](https://github.com/RustFields/ScaFi-fields/commit/a368d463260b39f9b87a85fb353d51f661994230))
* first language implementation ([c803b16](https://github.com/RustFields/ScaFi-fields/commit/c803b1633b6ef6a8becf7b43d3edb6dbd248845b))
* implement function to flatten fields ([b6f6f09](https://github.com/RustFields/ScaFi-fields/commit/b6f6f09127ec15bdba89744f220547329b167772))
* implement nbrf and repf ([6a1c9f5](https://github.com/RustFields/ScaFi-fields/commit/6a1c9f5ba4c52abb276f6473e2f9761aae5817c4))
* write is always true in nbrf ([327df39](https://github.com/RustFields/ScaFi-fields/commit/327df39fbce2b55bdf8f2788d03c9ca9be1363cd))


### Bug Fixes

* correct implementation ([77d098b](https://github.com/RustFields/ScaFi-fields/commit/77d098bcf5961c49e280a13298a29b9429874319))
* fix feature warnings ([9acb5e9](https://github.com/RustFields/ScaFi-fields/commit/9acb5e9247a5b5d0fbcd4b779633d28809af7a86))
* fix Field.fold implementation ([9dfa9fe](https://github.com/RustFields/ScaFi-fields/commit/9dfa9fe7e1433be10beff3fd8d3678f9c4700b80))
* fix implementation of repf ([17ca580](https://github.com/RustFields/ScaFi-fields/commit/17ca5806560baeb7ef6382f7d5a521e37cb27fa6))
* fix nbrf behavior ([256f0a1](https://github.com/RustFields/ScaFi-fields/commit/256f0a188da701149385c0ed45afb828ba31facc))
* from expr signature ([f1d4e54](https://github.com/RustFields/ScaFi-fields/commit/f1d4e54cf2a9ebe2f52518f153071e508f3a6115))
* modified write condition inside nbrf ([4f2bfc4](https://github.com/RustFields/ScaFi-fields/commit/4f2bfc406b6d4340281517ec3a2f7c3902624f94))
* remove enclosing brackets ([dbc99fd](https://github.com/RustFields/ScaFi-fields/commit/dbc99fd404a67dc9fe1b391e0d5897f519eb39ab))
* remove vm.isolate in foldhoodf ([6cc8b50](https://github.com/RustFields/ScaFi-fields/commit/6cc8b50509994bd474060b18c3344bf3bf30084c))
* **test:** setup correct program and expect correct result ([0ed4b09](https://github.com/RustFields/ScaFi-fields/commit/0ed4b095a2c2822bda3c11e3f7b7d2696909c36c))
* use locally inside folded eval ([d82dd01](https://github.com/RustFields/ScaFi-fields/commit/d82dd0192615d37e34ed30e5782862c83064316c))


### Dependency updates

* **deps:** update dependency sbt/sbt to v1.9.1 ([07733a3](https://github.com/RustFields/ScaFi-fields/commit/07733a30b4f7450ddeccf94937375b5b7478eaf7))
* **deps:** update dependency sbt/sbt to v1.9.2 ([591617c](https://github.com/RustFields/ScaFi-fields/commit/591617cea8837afe70510d36bcc02e0ead19a448))
* **deps:** update dependency sbt/sbt to v1.9.3 ([05a2e7b](https://github.com/RustFields/ScaFi-fields/commit/05a2e7b871be0fc21e8cf8fc72cfbd0db8fc2fa0))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.39 ([b06861e](https://github.com/RustFields/ScaFi-fields/commit/b06861edbd0db19a4e437b36d16338faca043722))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.40 ([6e7c33c](https://github.com/RustFields/ScaFi-fields/commit/6e7c33cb975568bc9ff84410ddab067152fdc3a8))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.41 ([cea6252](https://github.com/RustFields/ScaFi-fields/commit/cea6252342cb4383b29f90b32ce8566ce43363cc))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.43 ([f7800ed](https://github.com/RustFields/ScaFi-fields/commit/f7800edfdb681cf96a11fb3ac07b8c5cb6a5a5f5))
* **deps:** update node.js to 18.17 ([889728b](https://github.com/RustFields/ScaFi-fields/commit/889728b014f4c60c67d3ae9bf9dc9221cb3e8d80))


### Documentation

* add doc for Field library functions ([ded3e64](https://github.com/RustFields/ScaFi-fields/commit/ded3e641a67d672a11ec8a03ab8173db2befba87))
* add scaladoc for the functions ([bfd1cfb](https://github.com/RustFields/ScaFi-fields/commit/bfd1cfb0f250932851e5c3bccb02e1caeda1a2f1))


### Tests

* adapt tests to new nbrf behavior ([25978bf](https://github.com/RustFields/ScaFi-fields/commit/25978bf9b252cbc2a73ab111ebf92ffd4b1acc1b))
* add more tests to class TestByEquivalence ([f8547fd](https://github.com/RustFields/ScaFi-fields/commit/f8547fdc568707c2ae63d15fa602b11cc3ec6206))
* add simple test on language construct ([d2d32ef](https://github.com/RustFields/ScaFi-fields/commit/d2d32ef7a9f1b58929776896655128ab2ff8bb36))
* add test suite by round ([647619b](https://github.com/RustFields/ScaFi-fields/commit/647619b4ce1ec9887e39cfdcda9619c2d2585c95))
* add test utilities ([7d12077](https://github.com/RustFields/ScaFi-fields/commit/7d120774d90adfbea4f10f2be7888ed53c88426e))
* add tests in TestLangByRound ([e16c6ba](https://github.com/RustFields/ScaFi-fields/commit/e16c6bafe02c8bd104ffde6de0ad9e814f931216))
* add tests in TestLangByRound ([d7a2c98](https://github.com/RustFields/ScaFi-fields/commit/d7a2c987b5db96ea2641839dc679902ffb665152))
* completed test suite with the three operators ([73ed4ac](https://github.com/RustFields/ScaFi-fields/commit/73ed4acfef5a607a8bd2c23d7db363f91f5d3726))
* correct foldhoof test ([69b6762](https://github.com/RustFields/ScaFi-fields/commit/69b67622611be06a449a283344b48e47c85b0793))
* improve existing tests in TestLangByRound ([b67c45a](https://github.com/RustFields/ScaFi-fields/commit/b67c45a1f282aec3c6c107f2d06819f460781211))
* port part of tests in TestLangByRound ([97c3153](https://github.com/RustFields/ScaFi-fields/commit/97c3153cc936becb698138f5f3ef20532f5613cd))
* refactor test suite with new functions ([561e6b2](https://github.com/RustFields/ScaFi-fields/commit/561e6b2d782568f16aa72005d60e1299ce873326))
* remove comment over non-passing test ([571abef](https://github.com/RustFields/ScaFi-fields/commit/571abeffdd2ac3e9d8cb3eb11aa9e80486529ce8))
* remove test for nested foldhoodf ([7de70e3](https://github.com/RustFields/ScaFi-fields/commit/7de70e3564081895581e4369cd2ab0c8e276e632))
* update performance test with new foldhoodf signature ([8a1fcfd](https://github.com/RustFields/ScaFi-fields/commit/8a1fcfd69806bb86ec6c1521423dc218cbf86121))
* update test with Field Language ([e4c3d85](https://github.com/RustFields/ScaFi-fields/commit/e4c3d8558c31aa39c8670ca34d714a753d0a517e))
* update test with new foldhoodf signature ([bb4631e](https://github.com/RustFields/ScaFi-fields/commit/bb4631e38d4dc44fe768abdf38c05841c4376d6a))
* update test with new foldhoodf syntax, still miss some tests ([eada445](https://github.com/RustFields/ScaFi-fields/commit/eada4458e334958c54484e410694714d57461e17))


### Refactoring

* add fromExpression construct and refactor foldhoodf ([65b437c](https://github.com/RustFields/ScaFi-fields/commit/65b437c290d60c1bd9c1e21d6f5ea7156647579a))
* add implicit conversion to function ([9776c69](https://github.com/RustFields/ScaFi-fields/commit/9776c6984d8739f05e47a4a72bce9ecc178f4c83))
* change conversion construction function ([b67a219](https://github.com/RustFields/ScaFi-fields/commit/b67a2195f547fc2ab3d42e616d5483eb425ffd45))
* change foldhoodf and fromExpression signature to have a lambda as last curried argument ([067a57d](https://github.com/RustFields/ScaFi-fields/commit/067a57d8bbcf14f55071b99a5a9a36e8992f92a5))
* fold now isn't restricted to neighbouring fields ([b8dfeae](https://github.com/RustFields/ScaFi-fields/commit/b8dfeaee1dd0020eafce735e520a8a244db14ba4))
* make default a function that produces A ([1382ec7](https://github.com/RustFields/ScaFi-fields/commit/1382ec7d28f4a265a3f18d82d0cd6a0221d4f7a9))
* make FieldTest extend FieldLib ([d04cbb9](https://github.com/RustFields/ScaFi-fields/commit/d04cbb982a140fccbf7780af8ad4227f7f603ed2))
* Modify nbr signature and implementation ([719eed9](https://github.com/RustFields/ScaFi-fields/commit/719eed997481267f4730e0517e95eee19489b5db))
* refactor foldhoodf ([882bd86](https://github.com/RustFields/ScaFi-fields/commit/882bd865b6788b9e6ac4b1fd643b993c0eda1b70))
* refactor foldhoodf ([e2a2839](https://github.com/RustFields/ScaFi-fields/commit/e2a2839ddc219c8deb2f1db887308f2099393106))
* remove ambiguous function ([ea6cd99](https://github.com/RustFields/ScaFi-fields/commit/ea6cd996bc2f89e87ae8242ad2acea56f73d8e62))
* remove old test suite ([9642fcc](https://github.com/RustFields/ScaFi-fields/commit/9642fcc68e90d9994135f4c8cccee80e039d7b30))
* remove useless function ([8ed1f55](https://github.com/RustFields/ScaFi-fields/commit/8ed1f553c0757f9c76638fd1b90b34e10c89232f))
* remove useless implicit parameter ([d17dd1f](https://github.com/RustFields/ScaFi-fields/commit/d17dd1f82627ac3c2ceb920bc91c81c82949bb65))
* remove useless inheritance ([5287580](https://github.com/RustFields/ScaFi-fields/commit/52875806b53ed49e62db67f5adc5f696a8dc172d))
* rename trait and move to syntax package ([a734280](https://github.com/RustFields/ScaFi-fields/commit/a734280fca6a7a3c0a7e559e81846d84dc8c203c))
* use function to avoid duplicate code ([1bbfed8](https://github.com/RustFields/ScaFi-fields/commit/1bbfed82149c8c83a5b8ed8fa8541122f415fbf3))
* use this as test interpreter ([d4652f4](https://github.com/RustFields/ScaFi-fields/commit/d4652f4445ac7735a86f113c4c56bdd73808111f))
* used more general function for implementing selfValue ([f4396d9](https://github.com/RustFields/ScaFi-fields/commit/f4396d95c9eeccb3a390cbc375b8534b7e0f01b4))


### General maintenance

* add scalac options for deprecation and feature ([67824c9](https://github.com/RustFields/ScaFi-fields/commit/67824c9653349ed04888a5fdecee514b09fa904f))
* optimize imports ([6f4b817](https://github.com/RustFields/ScaFi-fields/commit/6f4b81762e118ba00253c181af32c004eb1ab4cc))
* optimized import ([df0125e](https://github.com/RustFields/ScaFi-fields/commit/df0125e7cd1cbdcc96bf54a786fe4828ba0b3a02))
* **test:** add imports ([5d8dafe](https://github.com/RustFields/ScaFi-fields/commit/5d8dafec02498659f54baa07227f3075d776c60f))
* **test:** add imports ([86466ea](https://github.com/RustFields/ScaFi-fields/commit/86466ea3b83dc9e828316e60b4d46c04725a8fd1))
* **test:** refactor tests ([e2c27a4](https://github.com/RustFields/ScaFi-fields/commit/e2c27a45426af68a161ef8bc14d65a096c8ffcc3))
* **test:** remove problematic test ([49fb8f6](https://github.com/RustFields/ScaFi-fields/commit/49fb8f6299d20f246d1b2494659368fdb5d7f0e2))

## [3.0.0](https://github.com/RustFields/ScaFi-fields/compare/2.0.0...3.0.0) (2023-06-26)


### ⚠ BREAKING CHANGES

* refactor dependencies

### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.38 ([a46a81d](https://github.com/RustFields/ScaFi-fields/commit/a46a81d76aa44b8fc8845129419d5f9d08c1f603))


### Refactoring

* refactor dependencies ([1b97768](https://github.com/RustFields/ScaFi-fields/commit/1b97768c938d1a6068dd4da40a9efd50dbf40858))

## [2.0.0](https://github.com/RustFields/ScaFi-fields/compare/1.1.0...2.0.0) (2023-06-20)


### ⚠ BREAKING CHANGES

* add organization packages

### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.32 ([3d62273](https://github.com/RustFields/ScaFi-fields/commit/3d622735719ae492055ce81d2184c4253f2abddb))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.33 ([20b9da2](https://github.com/RustFields/ScaFi-fields/commit/20b9da26ec91a1405349cb61f42ba02853fcc57c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.35 ([f53fb72](https://github.com/RustFields/ScaFi-fields/commit/f53fb7268a386488006a1e459fb82914c17057ce))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.37 ([51c1f22](https://github.com/RustFields/ScaFi-fields/commit/51c1f229bacd27b900a8b0fe3051392c0d99045f))


### General maintenance

* [skip ci] update version in build.sbt ([87ad11d](https://github.com/RustFields/ScaFi-fields/commit/87ad11d9a30b6af80dd1b4110d958370d773b83f))
* add .gitattributes ([befb46f](https://github.com/RustFields/ScaFi-fields/commit/befb46fc8f5361ca8b1958eee58043ea9f2d0c36))
* add organization packages ([f0a8b0b](https://github.com/RustFields/ScaFi-fields/commit/f0a8b0b356a6f222481f3180a2ed2c05384e195f))


### Build and continuous integration

* add prepareCommands in Semantic Release configuration ([4f69129](https://github.com/RustFields/ScaFi-fields/commit/4f69129a65a51b0f96f72fec9bafc114448a577a))
* add sbt-assembly plugin ([1387858](https://github.com/RustFields/ScaFi-fields/commit/138785883438d38663c454727a81e4c40971a1f3))
* add ScaFi-core dependency ([98ad243](https://github.com/RustFields/ScaFi-fields/commit/98ad2434366dbd3872b22bafce031fe2b9121edf))
* improve Semantic Release configuration ([d639a69](https://github.com/RustFields/ScaFi-fields/commit/d639a692d21472643e2e6e13e589d25a0b32d9ba))
* **Mergify:** configuration update ([02fe478](https://github.com/RustFields/ScaFi-fields/commit/02fe4783c840543a5aa71a29c997d8580a4aa18b))
* remove SBT Assembly plugin ([ebc5b79](https://github.com/RustFields/ScaFi-fields/commit/ebc5b796c2618b1049e768cac13f3e948f4f5b1b))

## [1.1.0](https://github.com/RustFields/ScaFi-fields/compare/1.0.0...1.1.0) (2023-06-07)


### Features

* add additional functionality for fields ([58c3277](https://github.com/RustFields/ScaFi-fields/commit/58c32770917c055ce55e2775ededc198f446d177))
* add Defaultable concept ([9c68ee2](https://github.com/RustFields/ScaFi-fields/commit/9c68ee2025cb4a66df795c0e98a5ccbe12ebf226))
* add Field concept ([2403e75](https://github.com/RustFields/ScaFi-fields/commit/2403e753db786362d878677a16fbe7a86afee756))


### General maintenance

* [skip ci] update version in build.sbt ([06b8abd](https://github.com/RustFields/ScaFi-fields/commit/06b8abd47f438b57e3d20642654339014a1b63f3))


### Dependency updates

* **deps:** update dependency sbt/sbt to v1.9.0 ([8899ce4](https://github.com/RustFields/ScaFi-fields/commit/8899ce42ea2c121bd1fe21c59b213dec5454a0ee))


### Build and continuous integration

* add cats and scalatest as dependency ([2053941](https://github.com/RustFields/ScaFi-fields/commit/20539414c6bbe5960b6e1662e9fd9ed93773d820))


### Tests

* add test suite for fields api ([8e0038e](https://github.com/RustFields/ScaFi-fields/commit/8e0038eda429398a87e09be1b879d7a7b4546b66))

## 1.0.0 (2023-06-07)


### Features

* config ci-cd ([cd247e5](https://github.com/RustFields/ScaFi-fields/commit/cd247e5121fdd3395ab01db98c468853ad9dcb16))
* create project ([c545755](https://github.com/RustFields/ScaFi-fields/commit/c54575516e07d9224b6c80c2966b50e30cbd4d94))


### General maintenance

* add gitignore ([c97d354](https://github.com/RustFields/ScaFi-fields/commit/c97d35477ef18c34f6bfd2895d523d02ccc226a8))
* install git hooks ([fad9d3f](https://github.com/RustFields/ScaFi-fields/commit/fad9d3f1206462e726174ede999e17ff0b4e01f4))


### Dependency updates

* **deps:** add renovate.json ([6f34df9](https://github.com/RustFields/ScaFi-fields/commit/6f34df9b39b413d3ae05edc8309d18909aa721f2))
