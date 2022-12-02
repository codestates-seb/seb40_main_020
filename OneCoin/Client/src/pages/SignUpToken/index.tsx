import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function SignUpToken() {
	const navigate = useNavigate();
	useEffect(() => {
		const url = new URL(window.location.href);
		const authorization = url.searchParams.get('authorization');
		const refresh = url.searchParams.get('refresh');
		if (authorization && refresh) {
			console.log(authorization);
			sessionStorage.setItem('login-token', `Bearer ${authorization}`);
			sessionStorage.setItem('login-refresh', refresh);
			navigate('/');
		}
	});
	return <></>;
}

export default SignUpToken;
