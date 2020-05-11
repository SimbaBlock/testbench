#testbench

# 通用 
	时间戳过时时间15秒                              
	加密signature：带有page字段的page不需要带入加密参数
	signature = sha256_HMAC(所有参数 ,"接口名称", key)
	所有参数依次:
		access_key，uid，tonce（获取用户资产）
		access_key，uid，change，tnonce（更新用户资产）
		access_key，uid，tonce（添加用户关联信息）
		access_key，tonce（查询所有用户资产）

# 获取用户资产		  						
    URL:http://localhost:8422/api/getBalance		
      传参:		
	  access_key	string		
	  uid		string	用户uid
	  tnonce	string	时间戳
	  signature	string	加密后的sign（sha256_HMAC加密）
	  currency  string  币种

# 更新用户资产			
    URL:http://localhost:8422/api/updateBalance
      传参:
	  access_key	string		
	  uid		string	用户uid
	  change	string       (正数代表加资产，负数代表减资产)
	  tnonce	string	时间戳
	  signature	string	加密后的sign（sha256_HMAC加密）
      currency  string  币种

# 添加用户关联信息			
    URL:http://localhost:8422/api/addUser
      传参:
	  access_key	string		
	  uid		string	用户uid
	  tnonce	string	时间戳
	  signature	string	加密后的sign（sha256_HMAC加密）

# 查询所有用户资产			
    URL:http://localhost:8422/api/getUserAllBalance
      传参:
	  access_key	string		
	  tnonce	string	时间戳
	  signature	string	加密后的sign（sha256_HMAC加密）
	  page		int      页数
	  currency  string  币种
