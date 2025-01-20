<!DOCTYPE html>
<html>
<head>
	<#include "authorize_common.ftl">

	<script type="text/javascript">
		$(function(){
			<#if true == isExtendAttr>
			var attrIndex = 0;

			function addExtendAttr(attribute,attributeValue){
				var html =  '<tr  id="extendTr_' + attrIndex + '"><td>'+attribute+'：</td><td>';
				html += '<input type="text" id="attribute_' + attrIndex + '" name="'+attribute+'" class="" title="" value="'+attributeValue+'"/>';
				html += '</td></tr>';

				$('#extendAttrBody').append(html);
				attrIndex++;
			}

			var extendAttrJson = eval("("+'${extendAttr}'+")");
			for(extendAttrIndex in extendAttrJson){
				addExtendAttr(extendAttrJson[extendAttrIndex].attr,extendAttrJson[extendAttrIndex].value);
			};
			</#if>

			$(document).ready(function () {
				$("#formbasedsubmit").submit(function (e) {
					e.preventDefault();

					var username = $("#identity_formbased_username").val();
					var password = $("#identity_formbased_password").val();

					if (!username || !password) {
						console.error("用户名和密码不能为空");
						return;
					}

					// 构建与 curl 命令完全相同的数据结构
					var formData = {
						description: "UI session",
						responseType: "cookie",
						username: username,
						password: password
					};

					console.log("发送的数据:", formData); // 添加调试日志
					console.log("token---------", getCSRFToken())

					var xhr = new XMLHttpRequest();
					xhr.open("POST", "http://sso.maxkey.top.local:9124/v3-public/activeDirectoryProviders/activedirectory?action=login", true);
					xhr.withCredentials = true;
					xhr.setRequestHeader("Content-Type", "application/json");
					xhr.setRequestHeader("Accept", "application/json");

					xhr.onload = function() {
						console.log("状态码:", xhr.status);
						console.log("响应头:", xhr.getAllResponseHeaders());
						console.log("响应内容:", xhr.responseText);

						if (xhr.status >= 200 && xhr.status < 300) {
							try {
								var response = JSON.parse(xhr.responseText);
								if (response.status === "success") {
									window.location.href = "${loginUrl}";
								} else {
									console.error("登录失败:", response.message);
								}
							} catch (e) {
								console.error("解析响应失败:", e);
							}
						} else if (xhr.status === 422) {
							console.error("数据格式错误 - 响应内容:", xhr.responseText);
						} else {
							console.error("请求失败:", xhr.status, xhr.statusText);
							console.error("响应内容:", xhr.responseText);
						}
					};

					xhr.onerror = function() {
						console.error("请求错误");
					};

					// 确保发送的是 JSON 字符串
					xhr.send(JSON.stringify(formData));
				});

				// 自动提交表单
				$("#formbasedsubmit").trigger("submit");
			});
		});
		function getCSRFToken() {
			var name = 'x-api-csrf=';
			var decodedCookie = decodeURIComponent(document.cookie);
			var ca = decodedCookie.split(';');
			for (var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) === ' ') {
					c = c.substring(1);
				}
				if (c.indexOf(name) === 0) {
					return c.substring(name.length, c.length);
				}
			}
			return '';
		}


	</script>
</head>

<body style="display:none">
<form id="formbasedsubmit" name="formbasedsubmit" method="post">
	<table>
		<tr>
			<td>username</td>
			<td><input type="text" id="identity_formbased_username" name="username" value="${username}" /></td>
		</tr>
		<tr>
			<td>password</td>
			<td><input type="password" id="identity_formbased_password" name="password" value="${password}" /></td>
		</tr>
		<tr>
			<td colspan="2">
				<table>
					<tbody id="extendAttrBody"></tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2"><input id="formbasedsubmitbutton" type="button" value="submit" /></td>
		</tr>
	</table>
</form>
</body>
</html>
