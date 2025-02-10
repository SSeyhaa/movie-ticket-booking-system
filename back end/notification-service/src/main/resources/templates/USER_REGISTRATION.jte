@param com.legend.common_util.dto.UserRegistrationJteParam userRegistrationJteParam

<!DOCTYPE html>
<html lang="en-US">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>user registration</title>
    <style>
        body {
            font-family: 'Segoe UI', Helvetica, Arial, sans-serif;
            line-height: 1.6;
            color: #2d3748;
            background-color: #f7fafc;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .header {
            background-color: #2563eb;
            background-image: linear-gradient(135deg, #2563eb, #3b82f6);
            color: white;
            padding: 40px 30px;
            text-align: center;
        }

        .header-title {
            font-size: 24px;
            margin: 0;
            font-weight: 600;
        }

        .content {
            padding: 40px 30px;
        }

        .welcome-text {
            font-size: 20px;
            color: #1a202c;
            margin-bottom: 20px;
        }

        .info-box {
            background-color: #f8fafc;
            border-left: 4px solid #4f46e5;
            padding: 15px;
            margin: 20px 0;
            border-radius: 0 4px 4px 0;
        }

        .support-email {
            display: inline-block;
            background-color: #f1f5f9;
            padding: 8px 16px;
            border-radius: 4px;
            color: #4f46e5;
            font-family: monospace;
            margin: 5px 0;
        }

        .divider {
            height: 1px;
            background-color: #e2e8f0;
            margin: 30px 0;
        }

        .footer {
            padding: 30px;
            background-color: #f8fafc;
            text-align: center;
            font-size: 14px;
            color: #64748b;
        }

        @media only screen and (max-width: 600px) {
            .container {
                width: 100%;
                border-radius: 0;
            }

            .header, .content, .footer {
                padding: 20px 15px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1 class="header-title">Welcome to Cinema KH!</h1>
    </div>

    <div class="content">
        <p class="welcome-text">Hi ${userRegistrationJteParam.getFirstName()} ${userRegistrationJteParam.getLastName()},</p>

        <p>We're excited to have you join our community! Your account has been successfully created.</p>

        <div class="info-box">
            <strong>Your registered email:</strong><br>
            ${userRegistrationJteParam.getEmail()}
        </div>

        <p>If you have any questions or need assistance, our support team is here to help at:</p>
        <span class="support-email">dev.kh@gmail.com</span>

        <div class="divider"></div>

        <p><strong>Note:</strong> If you didn't create this account, please contact us immediately.</p>
    </div>

    <div class="footer">
        <p>© ${java.time.Year.now().getValue()} Cinema KH. All rights reserved.</p>
        <p>This is an automated message. Please do not reply to this email.</p>
    </div>
</div>
</body>
</html>
