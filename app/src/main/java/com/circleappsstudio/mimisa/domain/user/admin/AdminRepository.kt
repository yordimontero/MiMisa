package com.circleappsstudio.mimisa.domain.user.admin

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource

class AdminRepository(private val adminDataSource: DataSource.AdminUser): Repository.AdminUser {

    override suspend fun createAdmin(
            emailUser: String,
            nameUser: String
    ) = adminDataSource.createAdmin(emailUser, nameUser)

    override suspend fun fetchAdminCode(): Resource<String?> = adminDataSource.fetchAdminCode()

}